package com.example.shoppingmall.util;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
	
    private static final String SECRET = "qowoqowo"; // yml에서 주입해도 OK
    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1시간

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public String getUsername(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
}
