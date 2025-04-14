package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductRequest {

    @Schema(description = "상품명", example = "무선 마우스")
    private String name;

    @Schema(description = "상품 설명", example = "조용한 클릭, 블루투스 지원")
    private String description;

    @Schema(description = "가격", example = "29000")
    private int price;

    @Schema(description = "재고 수량", example = "100")
    private int stock;
}
