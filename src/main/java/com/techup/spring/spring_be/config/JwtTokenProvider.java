package com.techup.spring.spring_be.config;


import com.techup.spring.spring_be.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 토큰 생성
    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        long expiresIn = 1000 * 60 * 60; // 1시간

        return Jwts.builder()
                .setSubject(user.getEmail())        // 기본 subject
                .claim("userName", user.getName())
                .claim("role", user.getRole())
                .claim("profileImage", user.getProfileImage())
                .claim("communityName", user.getCommunity().getName())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiresIn))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 이메일 꺼내기
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}