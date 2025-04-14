package com.example.shoppingmall.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.dto.ProductRequest;
import com.example.shoppingmall.dto.ProductResponse;
import com.example.shoppingmall.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품의 목록을 조회합니다.")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @PostMapping
    @Operation(summary = "상품 등록", description = "관리자가 새로운 상품을 등록합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok("상품이 등록되었습니다.");
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상품 정보를 조회합니다.")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품 ID를 통해 상품을 삭제합니다. (관리자만 가능)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다. (관리자만 가능)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok("상품이 수정되었습니다.");
    }

    
}
