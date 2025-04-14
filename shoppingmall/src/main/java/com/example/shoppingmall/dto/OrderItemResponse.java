package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품명", example = "무선 이어폰")
    private String productName;

    @Schema(description = "수량", example = "2")
    private int quantity;

    @Schema(description = "상품 단가", example = "99000")
    private int price;

    @Schema(description = "총 금액 (수량 × 단가)", example = "198000")
    private int totalPrice;
}
