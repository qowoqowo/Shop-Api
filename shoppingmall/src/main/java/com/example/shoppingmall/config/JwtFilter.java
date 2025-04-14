package com.example.shoppingmall.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.shoppingmall.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        // 1. Authorization 헤더에 "Bearer " 토큰이 있는지 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // 2. 토큰 유효성 검사
            if (jwtUtil.validateToken(token)) {
            	
            	// 3. 토큰에서 사용자명 추출
                String username = jwtUtil.getUsername(token);
                
                // 4. 사용자 정보 조회 (DB 조회)
                var userDetails = userDetailsService.loadUserByUsername(username);
                
                // 5. 인증 객체 생성
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 6. SecurityContext에 인증 객체 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // 7. 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}
