package com.beyond.ordersystem.product.service;

import com.beyond.ordersystem.member.domain.Member;
import com.beyond.ordersystem.member.repository.MemberRepository;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.dto.ProductCreateDto;
import com.beyond.ordersystem.product.dto.ProductResDto;
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
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    // 상품 등록
    public Long createProduct(ProductCreateDto dto) {
        if(productRepository.findByName(dto.getName()).isPresent()) throw new IllegalArgumentException("중복되는 이름입니다.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));

        Product product = productRepository.save(dto.toEntity(member));
        return product.getId();
    }

    // 상품목록 조회
    public List<ProductResDto> getProductList() {
        List<ProductResDto> productList =  productRepository.findAll().stream().map(a -> ProductResDto.fromEntity(a)).toList();
        return productList;
    }

    // 상품상세 조회
    public ProductResDto getProductDetail(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 상품입니다."));
        ProductResDto productResDto = ProductResDto.fromEntity(product);
        return productResDto;
    }

}
