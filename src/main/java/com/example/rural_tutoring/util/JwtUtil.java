package com.example.rural_tutoring.util;

import com.example.rural_tutoring.model.DTO.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_jwt_secret_key_must_be_at_least_256_bits_long_for_hs256";
    private static final long EXPIRATION_HOURS = 24; // 1天

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT令牌
     */
    public String generateToken(UserDTO userDto) {
        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRATION_HOURS, ChronoUnit.HOURS);
        
        return Jwts.builder()
                .subject(userDto.getId() + "")
                .claim("username", userDto.getUsername())
                .claim("openid", userDto.getOpenid())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 验证JWT令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.err.println("Token验证失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从JWT令牌中解析用户信息
     */
    public UserDTO parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            UserDTO user = new UserDTO();
            user.setId(Integer.valueOf(claims.getSubject()));
            user.setUsername(claims.get("username", String.class));
            user.setOpenid(claims.get("openid", String.class));
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Token解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从JWT令牌中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            return Integer.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException("从Token获取用户ID失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查JWT令牌是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // 解析失败认为已过期
        }
    }
} 