package com.example.shoppingmall.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @Schema(description = "주문할 상품 ID 리스트 (장바구니)", example = "[1, 3]")
    private List<Long> cartItemIds;
}
