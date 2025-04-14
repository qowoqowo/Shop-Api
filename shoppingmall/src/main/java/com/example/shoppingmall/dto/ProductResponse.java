package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "블루투스 이어폰")
    private String name;

    @Schema(description = "상품 설명", example = "노이즈 캔슬링 지원")
    private String description;

    @Schema(description = "가격", example = "99000")
    private int price;

    @Schema(description = "재고", example = "15")
    private int stock;
}
