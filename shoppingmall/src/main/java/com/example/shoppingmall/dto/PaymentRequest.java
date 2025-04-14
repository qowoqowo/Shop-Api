package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @Schema(description = "주문 ID", example = "1")
    private Long orderId;
    
    @Schema(description = "사용자 아이디", example = "qwer")
    private String username;
}
