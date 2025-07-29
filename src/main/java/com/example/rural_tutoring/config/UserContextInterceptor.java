package com.example.rural_tutoring.config;

import com.example.rural_tutoring.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserContextInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);
    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final ThreadLocal<Integer> currentUser = new ThreadLocal<>();
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        logger.info("拦截到请求: {} {}", method, requestURI);
        
        // 方式1: 从请求头获取用户ID（当前使用的方式）
        String userIdStr = request.getHeader(USER_ID_HEADER);
        logger.info("X-User-Id: {}", userIdStr);
        
        // 方式2: 从Authorization头获取JWT Token（推荐的方式）
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉"Bearer "前缀
            logger.info("Authorization Token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            
            try {
                // 验证JWT Token
                if (jwtUtil.validateToken(token)) {
                    // 从Token中解析用户ID
                    Integer userId = jwtUtil.getUserIdFromToken(token);
                    currentUser.set(userId);
                    logger.info("从JWT Token成功设置用户ID: {}", userId);
                    return true;
                } else {
                    logger.warn("JWT Token验证失败");
                }
            } catch (Exception e) {
                logger.error("JWT Token解析失败: {}", e.getMessage());
            }
        }
        
        // 如果JWT验证失败，回退到用户ID方式
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                Integer userId = Integer.valueOf(userIdStr);
                currentUser.set(userId);
                logger.info("从请求头成功设置用户ID: {}", userId);
            } catch (NumberFormatException e) {
                logger.error("用户ID格式错误: {}", userIdStr);
            }
        } else {
            logger.warn("请求头中没有用户ID或JWT Token");
        }
        
        return true; // 继续执行，不阻止请求
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理ThreadLocal，防止内存泄漏
        currentUser.remove();
        logger.info("请求完成，清理用户上下文");
    }
    
    /**
     * 获取当前线程的用户ID
     * 在业务方法中调用此方法获取当前登录用户ID
     */
    public static Integer getCurrentUserId() {
        Integer userId = currentUser.get();
        logger.info("获取当前用户ID: {}", userId);
        return userId;
    }
    
    /**
     * 设置当前线程的用户ID（用于测试或特殊情况）
     */
    public static void setCurrentUserId(Integer userId) {
        currentUser.set(userId);
        logger.info("手动设置用户ID: {}", userId);
    }
} 