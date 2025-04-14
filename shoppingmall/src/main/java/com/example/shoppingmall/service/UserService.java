package com.example.shoppingmall.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.dto.PasswordUpdateRequest;
import com.example.shoppingmall.dto.SignupRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원가입
    public void signup(SignupRequest request) {

        // 사용자 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용 중인 사용자 이름입니다.");
        }

        // 이메일 중복 체크 (선택)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 저장
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .role("USER") // 기본 권한 부여
                .build();

        userRepository.save(user);
    }
    
    // 비밀번호 변경
    @Transactional
    public void updatePassword(String username, PasswordUpdateRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호로 변경
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
	
}
