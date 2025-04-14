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
public class PasswordUpdateRequest {

    @Schema(description = "현재 비밀번호", example = "oldPassword123")
    private String currentPassword;

    @Schema(description = "새 비밀번호", example = "newPassword456")
    private String newPassword;
}
