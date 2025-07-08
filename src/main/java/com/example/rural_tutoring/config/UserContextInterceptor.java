package com.example.rural_tutoring.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserContextInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);
    public static final String USER_ID_HEADER = "X-User-Id";
    private static final ThreadLocal<Integer> currentUser = new ThreadLocal<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdStr = request.getHeader(USER_ID_HEADER);
        logger.info("拦截到请求: {} {}", request.getMethod(), request.getRequestURI());
        logger.info("X-User-Id: {}", userIdStr);
        
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                Integer userId = Integer.valueOf(userIdStr);
                currentUser.set(userId);
                logger.info("成功设置用户ID: {}", userId);
            } catch (NumberFormatException e) {
                logger.error("用户ID格式错误: {}", userIdStr);
            }
        } else {
            logger.warn("请求头中没有用户ID");
        }
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理ThreadLocal
        currentUser.remove();
        logger.info("请求完成，清理用户上下文");
    }
    
    public static Integer getCurrentUserId() {
        Integer userId = currentUser.get();
        logger.info("获取当前用户ID: {}", userId);
        return userId;
    }
} 