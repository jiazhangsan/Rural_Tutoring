package com.example.rural_tutoring.config;

import com.example.rural_tutoring.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 * 
 * 作用：
 * 1. 拦截所有需要认证的请求
 * 2. 从请求头中提取JWT Token
 * 3. 验证Token的有效性
 * 4. 设置Spring Security的认证上下文
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 跳过不需要认证的路径
        if (shouldSkipAuthentication(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 从Authorization头获取JWT Token
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // 去掉"Bearer "前缀
                
                // 验证JWT Token
                if (jwtUtil.validateToken(token)) {
                    // 从Token中获取用户ID
                    Integer userId = jwtUtil.getUserIdFromToken(token);
                    
                    // 创建认证对象
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userId.toString(), // principal
                            null,             // credentials
                            new ArrayList<>()  // authorities
                        );
                    
                    // 设置认证上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    System.out.println("JWT认证成功，用户ID: " + userId + ", 请求: " + method + " " + requestURI);
                } else {
                    System.out.println("JWT Token验证失败，请求: " + method + " " + requestURI);
                }
            } else {
                System.out.println("请求头中没有JWT Token，请求: " + method + " " + requestURI);
            }
        } catch (Exception e) {
            System.err.println("JWT认证过程中发生错误: " + e.getMessage());
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 判断是否需要跳过认证
     */
    private boolean shouldSkipAuthentication(String requestURI) {
        return requestURI.startsWith("/api/user/wxLogin") ||
               requestURI.startsWith("/api/user/register") ||
               requestURI.startsWith("/swagger-ui/") ||
               requestURI.startsWith("/v3/api-docs/") ||
               requestURI.startsWith("/api/frontend/") ||
               requestURI.startsWith("/api/videos/") ||
               requestURI.startsWith("/api/role/") ||
               requestURI.startsWith("/api/grade/") ||
               requestURI.startsWith("/api/subject/create");
    }
} 