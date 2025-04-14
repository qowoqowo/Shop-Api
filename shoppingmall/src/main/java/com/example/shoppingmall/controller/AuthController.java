package com.example.shoppingmall.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.dto.LoginRequest;
import com.example.shoppingmall.dto.LoginResponse;
import com.example.shoppingmall.dto.SignupRequest;
import com.example.shoppingmall.service.UserService;
import com.example.shoppingmall.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "인증 관련 API (로그인, 회원가입 등)")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "username, password를 이용해 새로운 사용자를 등록합니다.")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공!");
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "username, password를 이용해 JWT 토큰을 발급받습니다.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 사용자 인증 시도
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 인증 성공 시 JWT 발급
        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }
	
}
