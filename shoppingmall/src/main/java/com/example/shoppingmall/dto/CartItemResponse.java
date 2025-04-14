package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
	
    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품명", example = "무선 마우스")
    private String productName;

    @Schema(description = "장바구니에 담긴 수량", example = "2")
    private int quantity;
    
    @Schema(description = "상품 가격 (개당)", example = "35000")
    private int price;

    @Schema(description = "상품 총합 (가격 × 수량)", example = "70000")
    private int totalPrice;
    
}
