package com.example.shoppingmall.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.info.Info;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "쇼핑몰 API 문서",
                description = "Spring Boot + JWT 인증 기반 쇼핑몰 API 문서입니다.",
                version = "v1.0"
        ),
        security = {@SecurityRequirement(name = "bearerAuth")} // 전역 보안 적용
)
@SecurityScheme(
        name = "bearerAuth", // Swagger UI에서 "Authorize"와 연결되는 이름
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT" // 형식 지정 (선택이지만 Swagger에 표시됨)
)
public class SwaggerConfig {

    // 그룹핑 설정 (예: /api/user/** 만 필터링)
    @Bean
    GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/user/**")
                .build();
    }
    
    @Bean
    GroupedOpenApi authApi() {
    	return GroupedOpenApi.builder()
    			.group("auth")
    			.pathsToMatch("/api/auth/**")
    			.build();
    }

    @Bean
    GroupedOpenApi productApi() {
    	return GroupedOpenApi.builder()
    			.group("product")
    			.pathsToMatch("/api/products/**")
    			.build();
    }
 
    @Bean
    GroupedOpenApi cartApi() {
    	return GroupedOpenApi.builder()
    			.group("cart")
    			.pathsToMatch("/api/cart/**")
    			.build();
    }
    
    @Bean
    GroupedOpenApi orderApi() {
    	return GroupedOpenApi.builder()
    			.group("order")
    			.pathsToMatch("/api/orders/**")
    			.build();
    }
}
