package com.example.jpa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置类 (使用Springdoc OpenAPI替代SpringFox)
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot JPA API")
                        .description("Spring Boot JPA 示例项目API文档")
                        .version("1.0.0"));
    }
}