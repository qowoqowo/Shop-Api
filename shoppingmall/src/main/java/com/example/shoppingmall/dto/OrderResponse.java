package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    @Schema(description = "주문 ID", example = "1001")
    private Long orderId;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemResponse> items;

    @Schema(description = "총 주문 금액", example = "135000")
    private int totalPrice;

    @Schema(description = "주문일시", example = "2025-04-10T15:30:00")
    private LocalDateTime orderDate;
}
