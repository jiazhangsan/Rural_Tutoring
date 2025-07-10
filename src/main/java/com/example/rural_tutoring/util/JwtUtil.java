package com.example.rural_tutoring.util;

import com.example.rural_tutoring.model.DTO.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_jwt_secret";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1天

    public String generateToken(UserDTO userDto) {
        return Jwts.builder()
                .setSubject(userDto.getId() + "")
                .claim("username", userDto.getUsername())
                .claim("openid", userDto.getOpenid()) // 新增openid
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
} 