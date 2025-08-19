package com.beyond.ordersystem.ordering.service;

import com.beyond.ordersystem.common.dto.CommonSuccessDto;
import com.beyond.ordersystem.common.service.SseAlarmService;
import com.beyond.ordersystem.ordering.domain.OrderDetail;
import com.beyond.ordersystem.ordering.domain.OrderStatus;
import com.beyond.ordersystem.ordering.domain.Ordering;
import com.beyond.ordersystem.ordering.dto.OrderCreateDto;
import com.beyond.ordersystem.ordering.dto.OrderDetailDto;
import com.beyond.ordersystem.ordering.dto.OrderListResDto;
import com.beyond.ordersystem.ordering.dto.ProductDto;
import com.beyond.ordersystem.ordering.feignclient.ProductFeignClient;
import com.beyond.ordersystem.ordering.repository.OrderingDetailRepository;
import com.beyond.ordersystem.ordering.repository.OrderingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final OrderingDetailRepository orderingDetailRepository;
    private final SseAlarmService sseAlarmService;
    private final RestTemplate restTemplate;
    private final ProductFeignClient productFeignClient;

    // 주문 생성
    public Long createOrdering(List<OrderCreateDto> dtos, String email) {

        Ordering ordering = Ordering.builder().orderStatus(OrderStatus.ORDERED).memberEmail(email).build();
        orderingRepository.save(ordering);

        // 재고 조회
        for(OrderCreateDto dto : dtos) {
            // 재고 조회
            String productDetailUrl = "http://product-service/product/detail/"+dto.getProductId();
            HttpHeaders headers = new HttpHeaders();
            // HttpEntity: http body와 http header를 세팅하기 위한
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            // 원래는 아래 요청의 예외를 try/catch 해줘야 함
            ResponseEntity<CommonSuccessDto> responseEntity = restTemplate.exchange(productDetailUrl, HttpMethod.GET, httpEntity, CommonSuccessDto.class);
            CommonSuccessDto commonSuccessDto = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();

            // readValue : String -> class 변환
            // convertValue : Object 클래스 -> 클래스 변환
            ProductDto product = objectMapper.convertValue(commonSuccessDto.getResult(), ProductDto.class);


            int quantity = dto.getProductCount();

            // 재고 관리
//            boolean check = product.decreaseQuantity(quantity); // 조건은 여기서 해결하는게 나을 듯
//            if(!check) {
//                throw new IllegalArgumentException("재고가 부족합니다.");
//            }

            // 주문 발생
            OrderDetail orderDetail = OrderDetail.builder().productId(product.getId()).productName(product.getName()).quantity(quantity).ordering(ordering).build();
            ordering.getOrderDetailList().add(orderDetail);

            // 동기적 재고 감소 요청
            String productUpdateStockUrl = "http://product-service/product/updatestock";
            HttpHeaders stockHeaders = new HttpHeaders();
            stockHeaders.setContentType(MediaType.APPLICATION_JSON);
            // HttpEntity: http body와 http header를 세팅하기 위한
            HttpEntity<OrderCreateDto> updateStockEntity = new HttpEntity<>(dto, stockHeaders);
            restTemplate.exchange(productUpdateStockUrl, HttpMethod.PUT, updateStockEntity, Void.class);

        }

        sseAlarmService.publishMessage("admin@naver.com", email, ordering.getId());

        return ordering.getId();
    }

    public Long createFeignKafka(List<OrderCreateDto> dtos, String email) {

        Ordering ordering = Ordering.builder().orderStatus(OrderStatus.ORDERED).memberEmail(email).build();
        orderingRepository.save(ordering);

        // 재고 조회
        for(OrderCreateDto dto : dtos) {
            // feign 클라이언트를 사용한 상품 조회
            CommonSuccessDto commonSuccessDto = productFeignClient.getProductById(dto.getProductId());

            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto product = objectMapper.convertValue(commonSuccessDto.getResult(), ProductDto.class);

            int quantity = dto.getProductCount();
            // 주문 발생
            OrderDetail orderDetail = OrderDetail.builder().productId(product.getId()).productName(product.getName()).quantity(quantity).ordering(ordering).build();
            ordering.getOrderDetailList().add(orderDetail);

            // feign을 통한 동기적 재고감소 요청
            productFeignClient.updateProductStockQuantity(dto);



            // kafka를 활용한 비동기적 재고 감소

        }

        sseAlarmService.publishMessage("admin@naver.com", email, ordering.getId());

        return ordering.getId();
    }

    // 주문 목록 조회
    public List<OrderListResDto> getOrderingList() {
        List<OrderListResDto> orderListResDtoList = new ArrayList<>();
        List<Ordering> orderingList = orderingRepository.findAll();

        // Ordering (id, orderStatus), Member(memberEmail)
        // OrderDetail (detailId, productName, productCount)
        for(Ordering ordering : orderingList) {
            List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

            List<OrderDetail> orderDetailList = orderingDetailRepository.findByOrdering(ordering);
            for(OrderDetail orderDetail : orderDetailList) {
                orderDetailDtoList.add(OrderDetailDto.fromEntity(orderDetail));
            }
            OrderListResDto orderListResDto = OrderListResDto.fromEntity(ordering, orderDetailDtoList);
            orderListResDtoList.add(orderListResDto);
        }

        return orderListResDtoList;
    }

    // 나의 주문 목록 조회
    public List<OrderListResDto> getMyOrderingList(String email) {
        List<Ordering> orderingList = orderingRepository.findByMemberEmail(email);

        List<OrderListResDto> orderListResDtoList = new ArrayList<>();

        for(Ordering ordering : orderingList) {
            List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

            List<OrderDetail> orderDetailList = orderingDetailRepository.findByOrdering(ordering);
            for(OrderDetail orderDetail : orderDetailList) {
                orderDetailDtoList.add(OrderDetailDto.fromEntity(orderDetail));
            }
            OrderListResDto orderListResDto = OrderListResDto.fromEntity(ordering, orderDetailDtoList);
            orderListResDtoList.add(orderListResDto);
        }

        return orderListResDtoList;
    }

}
