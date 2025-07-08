package com.example.rural_tutoring.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {
    
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件大小限制
        factory.setMaxFileSize(DataSize.ofMegabytes(10));        // 单个文件最大10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));     // 总请求大小限制50MB，支持多文件上传
        // 设置文件上传临时目录
        factory.setLocation("temp");
        return factory.createMultipartConfig();
    }
} 