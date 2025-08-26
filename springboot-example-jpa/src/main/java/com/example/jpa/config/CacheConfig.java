package com.example.jpa.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置类
 * 启用 Spring Cache 功能
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // Spring Boot 会自动配置 EhCache 作为缓存提供者
    // 配置文件在 ehcache.xml 中定义
}
