package com.example.rural_tutoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserContextInterceptor())
                .addPathPatterns("/api/**")  // 拦截所有/api/开头的请求
                .excludePathPatterns("/api/auth/**"); // 排除登录等不需要用户信息的接口
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 