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
public class CartQuantityUpdateRequest {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "변경할 수량", example = "3")
    private int quantity;
}
