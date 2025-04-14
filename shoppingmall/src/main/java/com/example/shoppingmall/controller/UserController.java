package com.example.shoppingmall.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.dto.PasswordUpdateRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관련 기능 API")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    // 로그인한 유저 정보 조회
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "JWT를 통해 인증된 사용자의 정보를 반환합니다.")
    public User getMyUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }
    
    @PostMapping("/update-password")
    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호를 확인한 후 새 비밀번호로 변경합니다.")
    public ResponseEntity<String> updatePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody PasswordUpdateRequest request) {

        userService.updatePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok("비밀번호 변경 성공. 다시 로그인해주세요.");
    }
	
}
