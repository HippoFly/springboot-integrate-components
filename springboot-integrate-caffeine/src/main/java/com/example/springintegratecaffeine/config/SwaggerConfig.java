package com.example.springintegratecaffeine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Swagger API 文档配置
 * 使用 SpringDoc OpenAPI 3
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot Caffeine Cache API")
                        .description("Spring Boot 集成 Caffeine 缓存的完整示例项目\n\n" +
                                "本项目演示了 Caffeine 缓存的各种使用方式：\n" +
                                "- 手动缓存管理\n" +
                                "- Spring Cache 注解\n" +
                                "- 不同的过期策略\n" +
                                "- 缓存统计和监控\n" +
                                "- 性能测试和最佳实践")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Spring Boot Caffeine Example")
                                .url("https://github.com/ben-manes/caffeine")
                                .email("example@caffeine.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8080")
                                .description("开发环境"),
                        new Server()
                                .url("https://api.example.com")
                                .description("生产环境")
                ));
    }
}
