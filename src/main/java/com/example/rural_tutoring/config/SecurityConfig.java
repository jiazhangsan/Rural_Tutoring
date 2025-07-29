package com.example.rural_tutoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类
 * 
 * 作用：
 * 1. 配置哪些路径需要认证，哪些路径可以公开访问
 * 2. 配置密码加密方式
 * 3. 配置CSRF保护等安全策略
 * 4. 注册JWT认证过滤器
 * 
 * 与UserContextInterceptor的关系：
 * - SecurityConfig：决定请求是否能通过Spring Security的认证
 * - JwtAuthenticationFilter：执行JWT Token验证
 * - UserContextInterceptor：在请求通过Security后，进一步解析用户信息
 * - 三者配合使用，形成完整的安全体系
 */
@Configuration
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * 密码加密器
     * 用于对用户密码进行加密存储
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 安全过滤器链配置
     * 定义请求的访问规则
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // 禁用CSRF保护（因为是API服务）
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
            .authorizeHttpRequests(auth -> auth
                // 公开路径 - 不需要认证就可以访问
                .requestMatchers("/api/students/**").permitAll()      // 学生相关接口
                .requestMatchers("/api/user/**").permitAll()          // 用户相关接口（包括登录）
                .requestMatchers("/api/role/**").permitAll()          // 角色相关接口
                .requestMatchers("/api/teacher/**").permitAll()       // 老师相关接口
                .requestMatchers("/api/lesson/**").permitAll()        // 课程相关接口
                .requestMatchers("/api/homework/**").permitAll()      // 作业相关接口
                .requestMatchers("/api/subject/create").permitAll()   // 科目创建接口
                .requestMatchers("/api/frontend/**").permitAll()      // 前端资源接口
                .requestMatchers("/api/videos/**").permitAll()        // 视频上传接口
                .requestMatchers("/api/grade/**").permitAll()        // 年级管理接口

                // Swagger UI 相关路径 - 开发文档
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                
                // 管理员路径 - 需要ADMIN角色
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/teacher-verification-code/**").authenticated()  // 老师验证码管理接口 - 需要认证
                
                // 其他所有路径 - 需要认证（但这里实际上都放行了）
                .anyRequest().authenticated()
            )
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}