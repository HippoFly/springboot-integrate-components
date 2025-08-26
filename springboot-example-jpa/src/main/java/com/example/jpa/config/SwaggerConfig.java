package com.example.jpa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置类
 * 使用 SpringDoc OpenAPI 3 替代 Swagger 2
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot JPA 完整示例 API")
                        .version("1.0.0")
                        .description("这是一个完整的 Spring Boot JPA 示例项目的 API 文档，" +
                                "展示了 JPA 的各种高级特性和最佳实践，包括：\n\n" +
                                "- 基础 CRUD 操作\n" +
                                "- 继承映射和复合主键\n" +
                                "- 审计功能和缓存机制\n" +
                                "- 动态查询和 Criteria API\n" +
                                "- 事务管理和异常处理\n\n" +
                                "适合学习 JPA 的各个特性和企业级开发最佳实践。")
                        .contact(new Contact()
                                .name("JPA 示例项目")
                                .url("https://github.com/your-repo")
                                .email("your-email@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}