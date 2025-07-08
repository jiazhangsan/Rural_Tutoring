package com.example.rural_tutoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http.csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth
                 .requestMatchers("/api/students/**").permitAll()  // 公开路径
                 .requestMatchers("/api/user/**").permitAll()
                 .requestMatchers("/api/role/**").permitAll()
                 .requestMatchers("/api/teacher/**").permitAll()
                 .requestMatchers("/api/lesson/**").permitAll()   // 允许所有课程相关的接口访问
                 .requestMatchers("/api/homework/**").permitAll()   // 允许所有课程相关的接口访问
                 .requestMatchers("/admin/**").hasRole("ADMIN") // 管理员路径
                 // Swagger UI 相关路径
                 .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                 .anyRequest().authenticated()  // 其他路径需登录
         );
     return http.build();
    }
}