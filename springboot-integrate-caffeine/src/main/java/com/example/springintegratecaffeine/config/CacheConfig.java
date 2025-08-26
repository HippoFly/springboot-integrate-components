package com.example.springintegratecaffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存配置类
 * 演示各种缓存策略和配置选项
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 基础缓存 - 手动管理
     * 演示基本的缓存配置
     */
    @Bean("basicCache")
    public Cache<String, Object> basicCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入后经过固定时间过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                // 启用统计
                .recordStats()
                // 移除监听器
                .removalListener((RemovalListener<String, Object>) (key, value, cause) -> 
                    log.info("缓存移除 - Key: {}, Value: {}, Cause: {}", key, value, cause))
                .build();
    }
    
    /**
     * 自动加载缓存
     * 演示缓存穿透保护
     */
    @Bean("loadingCache")
    public LoadingCache<String, String> loadingCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(500)
                .recordStats()
                .build(key -> {
                    // 模拟数据加载
                    log.info("Loading data for key: {}", key);
                    Thread.sleep(100); // 模拟IO操作
                    return "Loaded value for " + key;
                });
    }
    
    /**
     * 访问后过期缓存
     * 演示不同的过期策略
     */
    @Bean("accessCache")
    public Cache<String, Object> accessCache() {
        return Caffeine.newBuilder()
                // 设置最后一次访问后经过固定时间过期
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .initialCapacity(50)
                .maximumSize(200)
                .recordStats()
                .build();
    }
    
    /**
     * 基于权重的缓存
     * 演示基于内存大小的缓存管理
     */
    @Bean("weightCache")
    public Cache<String, Object> weightCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                // 设置最大权重而不是最大数量
                .maximumWeight(10_000)
                // 权重计算器
                .weigher((String key, Object value) -> {
                    // 简单的权重计算：字符串长度
                    return key.length() + value.toString().length();
                })
                .recordStats()
                .build();
    }

    /**
     * 主缓存管理器 - 用于 @Cacheable 注解
     * 配置默认的缓存策略
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次访问后经过固定时间过期
                .expireAfterAccess(30, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                // 启用统计
                .recordStats()
                // 移除监听器
                .removalListener((RemovalListener<Object, Object>) (key, value, cause) -> 
                    log.debug("Spring Cache 移除 - Key: {}, Cause: {}", key, cause)));
        return cacheManager;
    }
    
    /**
     * 用户信息专用缓存管理器
     * 针对用户数据的特殊配置
     */
    @Bean("userCacheManager")
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("userInfo", "userList");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(2000)
                .recordStats()
                .removalListener((RemovalListener<Object, Object>) (key, value, cause) -> 
                    log.info("用户缓存移除 - Key: {}, Cause: {}", key, cause)));
        return cacheManager;
    }
    
    /**
     * 短期缓存管理器
     * 用于临时数据缓存
     */
    @Bean("shortTermCacheManager")
    public CacheManager shortTermCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(500)
                .recordStats());
        return cacheManager;
    }

    /**
     * 缓存统计信息获取方法
     */
    public void printCacheStats() {
        Cache<String, Object> basicCache = basicCache();
        CacheStats stats = basicCache.stats();
        
        log.info("=== 缓存统计信息 ===");
        log.info("请求次数: {}", stats.requestCount());
        log.info("命中次数: {}", stats.hitCount());
        log.info("命中率: {:.2f}%", stats.hitRate() * 100);
        log.info("未命中次数: {}", stats.missCount());
        log.info("加载次数: {}", stats.loadCount());
        log.info("平均加载时间: {:.2f}ms", stats.averageLoadPenalty() / 1_000_000.0);
        log.info("驱逐次数: {}", stats.evictionCount());
    }
}