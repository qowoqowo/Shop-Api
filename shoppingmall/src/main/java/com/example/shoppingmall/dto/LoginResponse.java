package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}