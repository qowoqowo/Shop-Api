package com.example.shoppingmall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

	@Schema(description = "사용자 아이디", example = "qowoqowo")
	private String username;
	
	@Schema(description = "비밀번호", example = "1234")
	private String password;
	
}
