package com.beyond.ordersystem.product.controller;

import com.beyond.ordersystem.common.dto.CommonSuccessDto;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.dto.ProductCreateDto;
import com.beyond.ordersystem.product.dto.ProductResDto;
import com.beyond.ordersystem.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    // 상품 등록
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductCreateDto dto) {
        Long id = productService.createProduct(dto);

        return new ResponseEntity<>(new CommonSuccessDto(id, HttpStatus.CREATED.value(), "상품등록 성공"), HttpStatus.CREATED);
    }

    // 상품목록 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getProductList() {
        List<ProductResDto> productList = productService.getProductList();
        return new ResponseEntity<>(new CommonSuccessDto(productList, HttpStatus.OK.value(), "상풍목록 조회 성공"), HttpStatus.OK);
    }

    // 상품상세 조회
    @GetMapping("/detail/{inputId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long inputId) {
        ProductResDto productResDto = productService.getProductDetail(inputId);
        return new ResponseEntity<>(new CommonSuccessDto(productResDto, HttpStatus.OK.value(), "상품상세 조회 성공"), HttpStatus.OK);
    }

}
