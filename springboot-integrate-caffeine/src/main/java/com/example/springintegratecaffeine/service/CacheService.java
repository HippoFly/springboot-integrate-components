package com.example.springintegratecaffeine.service;

import com.example.springintegratecaffeine.entity.UserInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务类
 * 演示各种缓存操作和最佳实践
 */
@Slf4j
@Service
public class CacheService {

    @Resource
    @Qualifier("basicCache")
    private Cache<String, Object> basicCache;

    @Resource
    @Qualifier("loadingCache")
    private LoadingCache<String, String> loadingCache;

    @Resource
    @Qualifier("accessCache")
    private Cache<String, Object> accessCache;

    @Resource
    @Qualifier("weightCache")
    private Cache<String, Object> weightCache;

    /**
     * 基础缓存操作演示
     */
    public void basicCacheDemo() {
        log.info("=== 基础缓存操作演示 ===");
        
        // 1. 存储数据
        basicCache.put("user:1", createSampleUser(1));
        basicCache.put("user:2", createSampleUser(2));
        
        // 2. 获取数据
        UserInfo user1 = (UserInfo) basicCache.getIfPresent("user:1");
        log.info("获取用户1: {}", user1);
        
        // 3. 获取数据（如果不存在则计算）
        Object user3 = basicCache.get("user:3", key -> {
            log.info("缓存未命中，创建用户: {}", key);
            return createSampleUser(3);
        });
        log.info("获取用户3: {}", user3);
        
        // 4. 批量操作
        Map<String, Object> users = new HashMap<>();
        users.put("user:4", createSampleUser(4));
        users.put("user:5", createSampleUser(5));
        basicCache.putAll(users);
        
        // 5. 获取所有键
        Set<String> keys = basicCache.asMap().keySet();
        log.info("缓存中的所有键: {}", keys);
        
        // 6. 清理操作
        basicCache.invalidate("user:1");
        basicCache.invalidateAll(Arrays.asList("user:2", "user:3"));
        
        log.info("基础缓存操作完成");
    }

    /**
     * 自动加载缓存演示
     */
    public void loadingCacheDemo() {
        log.info("=== 自动加载缓存演示 ===");
        
        try {
            // 1. 获取数据（自动加载）
            String value1 = loadingCache.get("key1");
            log.info("获取key1: {}", value1);
            
            // 2. 再次获取（从缓存）
            String value1Again = loadingCache.get("key1");
            log.info("再次获取key1: {}", value1Again);
            
            // 3. 批量获取
            Map<String, String> values = loadingCache.getAll(Arrays.asList("key2", "key3", "key4"));
            log.info("批量获取结果: {}", values);
            
            // 4. 异步获取
            String asyncValue = loadingCache.get("async_key", 
                key -> {
                    try {
                        Thread.sleep(200);
                        return "Async loaded value for " + key;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "Error loading " + key;
                    }
                });
            log.info("异步获取结果: {}", asyncValue);
            
        } catch (Exception e) {
            log.error("自动加载缓存演示出错", e);
        }
        
        log.info("自动加载缓存演示完成");
    }

    /**
     * 访问时间缓存演示
     */
    public void accessCacheDemo() {
        log.info("=== 访问时间缓存演示 ===");
        
        // 1. 添加数据
        accessCache.put("session:user1", createSampleUser(1));
        accessCache.put("session:user2", createSampleUser(2));
        
        // 2. 访问数据（刷新访问时间）
        Object user1 = accessCache.getIfPresent("session:user1");
        log.info("访问用户1会话: {}", user1 != null ? "存在" : "不存在");
        
        // 3. 模拟时间流逝和访问模式
        try {
            Thread.sleep(1000);
            
            // 继续访问user1，但不访问user2
            accessCache.getIfPresent("session:user1");
            log.info("刷新用户1会话访问时间");
            
            Thread.sleep(2000);
            
            // 检查缓存状态
            log.info("用户1会话: {}", accessCache.getIfPresent("session:user1") != null ? "存在" : "已过期");
            log.info("用户2会话: {}", accessCache.getIfPresent("session:user2") != null ? "存在" : "已过期");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("访问时间缓存演示完成");
    }

    /**
     * 权重缓存演示
     */
    public void weightCacheDemo() {
        log.info("=== 权重缓存演示 ===");
        
        // 1. 添加不同大小的对象
        UserInfo smallUser = UserInfo.builder()
            .id(1).name("小明").email("xm@test.com")
            .build();
            
        UserInfo largeUser = UserInfo.builder()
            .id(2).name("张三丰")
            .email("zhangsanfeng@wudang.com")
            .department("武当派掌门")
            .position("太极拳创始人")
            .tags(Arrays.asList("武林高手", "道教宗师", "太极拳", "内功心法"))
            .permissions(Arrays.asList("read", "write", "admin", "master"))
            .build();
        
        weightCache.put("small_user", smallUser);
        weightCache.put("large_user", largeUser);
        
        log.info("小用户对象大小: {}", smallUser.calculateSize());
        log.info("大用户对象大小: {}", largeUser.calculateSize());
        
        // 2. 添加更多数据直到触发驱逐
        for (int i = 3; i <= 100; i++) {
            UserInfo user = createSampleUser(i);
            weightCache.put("user:" + i, user);
        }
        
        // 3. 检查缓存状态
        log.info("小用户是否还在缓存: {}", weightCache.getIfPresent("small_user") != null);
        log.info("大用户是否还在缓存: {}", weightCache.getIfPresent("large_user") != null);
        
        log.info("权重缓存演示完成");
    }

    /**
     * 缓存统计信息演示
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 基础缓存统计
        CacheStats basicStats = basicCache.stats();
        Map<String, Object> basicCacheStats = new HashMap<>();
        basicCacheStats.put("requestCount", basicStats.requestCount());
        basicCacheStats.put("hitCount", basicStats.hitCount());
        basicCacheStats.put("hitRate", String.format("%.2f%%", basicStats.hitRate() * 100));
        basicCacheStats.put("missCount", basicStats.missCount());
        basicCacheStats.put("loadCount", basicStats.loadCount());
        basicCacheStats.put("evictionCount", basicStats.evictionCount());
        basicCacheStats.put("averageLoadPenalty", String.format("%.2fms", basicStats.averageLoadPenalty() / 1_000_000.0));
        stats.put("basicCache", basicCacheStats);
        
        // 自动加载缓存统计
        CacheStats loadingStats = loadingCache.stats();
        Map<String, Object> loadingCacheStats = new HashMap<>();
        loadingCacheStats.put("requestCount", loadingStats.requestCount());
        loadingCacheStats.put("hitCount", loadingStats.hitCount());
        loadingCacheStats.put("hitRate", String.format("%.2f%%", loadingStats.hitRate() * 100));
        loadingCacheStats.put("missCount", loadingStats.missCount());
        loadingCacheStats.put("loadCount", loadingStats.loadCount());
        loadingCacheStats.put("evictionCount", loadingStats.evictionCount());
        stats.put("loadingCache", loadingCacheStats);
        
        // 访问缓存统计
        CacheStats accessStats = accessCache.stats();
        Map<String, Object> accessCacheStats = new HashMap<>();
        accessCacheStats.put("requestCount", accessStats.requestCount());
        accessCacheStats.put("hitCount", accessStats.hitCount());
        accessCacheStats.put("hitRate", String.format("%.2f%%", accessStats.hitRate() * 100));
        accessCacheStats.put("missCount", accessStats.missCount());
        accessCacheStats.put("evictionCount", accessStats.evictionCount());
        stats.put("accessCache", accessCacheStats);
        
        // 权重缓存统计
        CacheStats weightStats = weightCache.stats();
        Map<String, Object> weightCacheStats = new HashMap<>();
        weightCacheStats.put("requestCount", weightStats.requestCount());
        weightCacheStats.put("hitCount", weightStats.hitCount());
        weightCacheStats.put("hitRate", String.format("%.2f%%", weightStats.hitRate() * 100));
        weightCacheStats.put("missCount", weightStats.missCount());
        weightCacheStats.put("evictionCount", weightStats.evictionCount());
        stats.put("weightCache", weightCacheStats);
        
        return stats;
    }

    /**
     * 缓存性能测试
     */
    public Map<String, Object> performanceTest(int iterations) {
        log.info("开始缓存性能测试，迭代次数: {}", iterations);
        
        Map<String, Object> results = new HashMap<>();
        
        // 1. 写入性能测试
        long writeStartTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            basicCache.put("perf_test:" + i, createSampleUser(i));
        }
        long writeEndTime = System.nanoTime();
        long writeTime = TimeUnit.NANOSECONDS.toMillis(writeEndTime - writeStartTime);
        
        // 2. 读取性能测试（命中）
        long readHitStartTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            basicCache.getIfPresent("perf_test:" + i);
        }
        long readHitEndTime = System.nanoTime();
        long readHitTime = TimeUnit.NANOSECONDS.toMillis(readHitEndTime - readHitStartTime);
        
        // 3. 读取性能测试（未命中）
        long readMissStartTime = System.nanoTime();
        for (int i = iterations; i < iterations * 2; i++) {
            basicCache.getIfPresent("perf_test:" + i);
        }
        long readMissEndTime = System.nanoTime();
        long readMissTime = TimeUnit.NANOSECONDS.toMillis(readMissEndTime - readMissStartTime);
        
        results.put("writeTime", writeTime + "ms");
        results.put("readHitTime", readHitTime + "ms");
        results.put("readMissTime", readMissTime + "ms");
        results.put("writeOpsPerSecond", iterations * 1000L / writeTime);
        results.put("readHitOpsPerSecond", iterations * 1000L / readHitTime);
        results.put("readMissOpsPerSecond", iterations * 1000L / readMissTime);
        
        log.info("缓存性能测试完成: {}", results);
        return results;
    }

    /**
     * 清理所有缓存
     */
    public void clearAllCaches() {
        log.info("清理所有缓存");
        
        basicCache.invalidateAll();
        loadingCache.invalidateAll();
        accessCache.invalidateAll();
        weightCache.invalidateAll();
        
        log.info("所有缓存已清理完成");
    }

    /**
     * 获取缓存健康状态
     */
    public Map<String, Object> getCacheHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        
        // 检查基础缓存
        health.put("basicCache", getCacheHealth("basicCache", basicCache));
        health.put("loadingCache", getCacheHealth("loadingCache", loadingCache));
        health.put("accessCache", getCacheHealth("accessCache", accessCache));
        health.put("weightCache", getCacheHealth("weightCache", weightCache));
        
        // 整体健康状态
        boolean allHealthy = health.values().stream()
                .allMatch(status -> "HEALTHY".equals(((Map<?, ?>) status).get("status")));
        
        health.put("overall", allHealthy ? "HEALTHY" : "WARNING");
        health.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return health;
    }

    /**
     * 获取单个缓存的健康状态
     */
    private Map<String, Object> getCacheHealth(String cacheName, Cache<?, ?> cache) {
        CacheStats stats = cache.stats();
        Map<String, Object> health = new HashMap<>();
        
        boolean isHealthy = true;
        List<String> issues = new ArrayList<>();
        
        // 检查命中率（如果有足够的请求）
        if (stats.requestCount() > 100 && stats.hitRate() < 0.7) {
            isHealthy = false;
            issues.add(String.format("命中率过低: %.2f%%", stats.hitRate() * 100));
        }
        
        // 检查平均加载时间
        if (stats.averageLoadPenalty() / 1_000_000.0 > 100.0) {
            isHealthy = false;
            issues.add(String.format("平均加载时间过长: %.2fms", stats.averageLoadPenalty() / 1_000_000.0));
        }
        
        health.put("status", isHealthy ? "HEALTHY" : "WARNING");
        health.put("issues", issues);
        health.put("size", cache.estimatedSize());
        health.put("hitRate", String.format("%.2f%%", stats.hitRate() * 100));
        health.put("evictionCount", stats.evictionCount());
        
        return health;
    }

    /**
     * 创建示例用户
     */
    private UserInfo createSampleUser(int id) {
        return UserInfo.builder()
            .id(id)
            .name("用户" + id)
            .sex(id % 2 == 0 ? "女" : "男")
            .age(20 + id % 40)
            .email("user" + id + "@example.com")
            .phone("138" + String.format("%08d", id))
            .department(getDepartment(id % 5))
            .position(getPosition(id % 3))
            .salary(8000.0 + id * 100)
            .hireDate(LocalDateTime.now().minusDays(id * 10))
            .lastLoginTime(LocalDateTime.now().minusHours(id % 24))
            .status(UserInfo.UserStatus.ACTIVE)
            .tags(Arrays.asList("tag" + (id % 3), "tag" + (id % 5)))
            .permissions(Arrays.asList("read", id % 2 == 0 ? "write" : "readonly"))
            .build();
    }

    private String getDepartment(int index) {
        String[] departments = {"技术部", "产品部", "运营部", "市场部", "人事部"};
        return departments[index];
    }

    private String getPosition(int index) {
        String[] positions = {"工程师", "经理", "专员"};
        return positions[index];
    }
}
