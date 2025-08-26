package com.example.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * JPA 审计配置类
 * 启用 JPA 审计功能，自动填充审计字段
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
    
    /**
     * 审计员提供者，用于获取当前操作用户
     * 在实际项目中，可以从 Spring Security 上下文中获取当前用户
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
    
    /**
     * 审计员提供者实现类
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {
        
        @Override
        @NonNull
        public Optional<String> getCurrentAuditor() {
            // 在实际项目中，这里应该从 Spring Security 上下文中获取当前用户
            // 这里为了演示，返回一个固定值
            return Optional.of("system");
        }
    }
}
