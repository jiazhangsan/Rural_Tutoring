package com.example.rural_tutoring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 配置类
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("乡村支教管理系统 API")
                        .description("基于Spring Boot 3.x的乡村支教管理系统RESTful API")
                        .version("v1.0.0"));
    }
} 