package com.example.shoppingmall.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.dto.ProductRequest;
import com.example.shoppingmall.dto.ProductResponse;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 모든 상품 조회
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .build())
                .toList();
    }
    
    // 상품 등록
    public void createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        productRepository.save(product);
    }

    // 상품 상세 조회
    public ProductResponse getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
    
    // 상품 삭제
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }
    
    // 상품 수정
    public void updateProduct(Long id, ProductRequest request) {
    	Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    	
    	product.setName(request.getName());
    	product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
    	
        productRepository.save(product);
    }
    
    
}
