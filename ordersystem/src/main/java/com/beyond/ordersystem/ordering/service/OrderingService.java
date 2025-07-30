package com.beyond.ordersystem.ordering.service;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.repository.MemberRepository;
import com.beyond.ordersystem.ordering.domain.OrderDetail;
import com.beyond.ordersystem.ordering.domain.OrderStatus;
import com.beyond.ordersystem.ordering.domain.Ordering;
import com.beyond.ordersystem.ordering.dto.OrderCreateDto;
import com.beyond.ordersystem.ordering.repository.OrderingDetailRepository;
import com.beyond.ordersystem.ordering.repository.OrderingRepository;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderingService {
    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderingDetailRepository orderingDetailRepository;

    // 주문 생성
    public Long createOrdering(List<OrderCreateDto> dtos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));

        // 주문 먼저 저장하고
        Ordering ordering = Ordering.builder().orderStatus(OrderStatus.ORDERED).member(member).build();
        orderingRepository.save(ordering);

        // 저장한 주문번호 가져와서, OrderingDetail 저장
        // product id 뽑아서 product 객체 추출
        for(OrderCreateDto dto : dtos) {
            Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new EntityNotFoundException("없는 상품입니다."));
            int quantity = dto.getProductCount();
            OrderDetail orderDetail = OrderDetail.builder().product(product).quantity(quantity).ordering(ordering).build();

            // @OneToMany + Cascade 조합으로 따로 save 없이 저장될 수 있게
            ordering.getOrderDetailList().add(orderDetail);

            // 재고 관리
            boolean check = product.decreaseQuantity(quantity);
            if(!check) {
                throw new IllegalArgumentException("요구 주문수량이 너무 많습니다.");
            }
        }

        return ordering.getId();
    }

}
