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
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 缓存性能测试服务
 * 提供各种性能测试场景和基准测试
 */
@Slf4j
@Service
public class PerformanceTestService {

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
    private Cache<String, UserInfo> weightCache;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 综合性能测试
     */
    public Map<String, Object> comprehensivePerformanceTest() {
        log.info("开始执行综合性能测试...");
        
        Map<String, Object> results = new HashMap<>();
        
        // 1. 基础缓存性能测试
        results.put("basicCacheTest", basicCachePerformanceTest());
        
        // 2. 自动加载缓存性能测试
        results.put("loadingCacheTest", loadingCachePerformanceTest());
        
        // 3. 并发性能测试
        results.put("concurrencyTest", concurrencyPerformanceTest());
        
        // 4. 内存使用测试
        results.put("memoryTest", memoryUsageTest());
        
        // 5. 缓存命中率测试
        results.put("hitRateTest", hitRateTest());
        
        log.info("综合性能测试完成");
        return results;
    }

    /**
     * 基础缓存性能测试
     */
    public Map<String, Object> basicCachePerformanceTest() {
        log.info("执行基础缓存性能测试...");
        
        Map<String, Object> result = new HashMap<>();
        int testSize = 10000;
        
        // 清空缓存
        basicCache.invalidateAll();
        
        // 写入性能测试
        long writeStartTime = System.nanoTime();
        for (int i = 0; i < testSize; i++) {
            UserInfo user = createTestUser(i);
            basicCache.put("user:" + i, user);
        }
        long writeEndTime = System.nanoTime();
        long writeTime = (writeEndTime - writeStartTime) / 1_000_000; // 转换为毫秒
        
        // 读取性能测试
        long readStartTime = System.nanoTime();
        for (int i = 0; i < testSize; i++) {
            basicCache.getIfPresent("user:" + i);
        }
        long readEndTime = System.nanoTime();
        long readTime = (readEndTime - readStartTime) / 1_000_000;
        
        // 统计信息
        CacheStats stats = basicCache.stats();
        
        result.put("testSize", testSize);
        result.put("writeTimeMs", writeTime);
        result.put("readTimeMs", readTime);
        result.put("writeOpsPerSecond", testSize * 1000L / writeTime);
        result.put("readOpsPerSecond", testSize * 1000L / readTime);
        result.put("hitRate", stats.hitRate());
        result.put("missRate", stats.missRate());
        result.put("cacheSize", basicCache.estimatedSize());
        
        log.info("基础缓存性能测试完成 - 写入: {}ms, 读取: {}ms", writeTime, readTime);
        return result;
    }

    /**
     * 自动加载缓存性能测试
     */
    public Map<String, Object> loadingCachePerformanceTest() {
        log.info("执行自动加载缓存性能测试...");
        
        Map<String, Object> result = new HashMap<>();
        int testSize = 5000;
        
        // 清空缓存
        loadingCache.invalidateAll();
        
        // 首次加载性能测试（缓存未命中）
        long firstLoadStartTime = System.nanoTime();
        for (int i = 0; i < testSize; i++) {
            try {
                loadingCache.get("load_test:" + i);
            } catch (Exception e) {
                log.error("加载缓存失败", e);
            }
        }
        long firstLoadEndTime = System.nanoTime();
        long firstLoadTime = (firstLoadEndTime - firstLoadStartTime) / 1_000_000;
        
        // 二次读取性能测试（缓存命中）
        long secondReadStartTime = System.nanoTime();
        for (int i = 0; i < testSize; i++) {
            try {
                loadingCache.get("load_test:" + i);
            } catch (Exception e) {
                log.error("读取缓存失败", e);
            }
        }
        long secondReadEndTime = System.nanoTime();
        long secondReadTime = (secondReadEndTime - secondReadStartTime) / 1_000_000;
        
        // 批量获取性能测试
        List<String> keys = IntStream.range(0, 1000)
                .mapToObj(i -> "load_test:" + i)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        long batchStartTime = System.nanoTime();
        try {
            loadingCache.getAll(keys);
        } catch (Exception e) {
            log.error("批量获取失败", e);
        }
        long batchEndTime = System.nanoTime();
        long batchTime = (batchEndTime - batchStartTime) / 1_000_000;
        
        CacheStats stats = loadingCache.stats();
        
        result.put("testSize", testSize);
        result.put("firstLoadTimeMs", firstLoadTime);
        result.put("secondReadTimeMs", secondReadTime);
        result.put("batchTimeMs", batchTime);
        result.put("loadOpsPerSecond", testSize * 1000L / firstLoadTime);
        result.put("hitOpsPerSecond", testSize * 1000L / secondReadTime);
        result.put("hitRate", stats.hitRate());
        result.put("averageLoadPenalty", stats.averageLoadPenalty() / 1_000_000); // 转换为毫秒
        result.put("loadCount", stats.loadCount());
        
        log.info("自动加载缓存性能测试完成 - 首次加载: {}ms, 二次读取: {}ms", firstLoadTime, secondReadTime);
        return result;
    }

    /**
     * 并发性能测试
     */
    public Map<String, Object> concurrencyPerformanceTest() {
        log.info("执行并发性能测试...");
        
        Map<String, Object> result = new HashMap<>();
        int threadCount = 10;
        int operationsPerThread = 1000;
        
        // 清空缓存
        basicCache.invalidateAll();
        
        // 并发写入测试
        CountDownLatch writeLatch = new CountDownLatch(threadCount);
        long writeStartTime = System.nanoTime();
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executorService.submit(() -> {
                try {
                    for (int i = 0; i < operationsPerThread; i++) {
                        String key = "concurrent_write:" + threadId + ":" + i;
                        UserInfo user = createTestUser(threadId * operationsPerThread + i);
                        basicCache.put(key, user);
                    }
                } finally {
                    writeLatch.countDown();
                }
            });
        }
        
        try {
            writeLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long writeEndTime = System.nanoTime();
        long concurrentWriteTime = (writeEndTime - writeStartTime) / 1_000_000;
        
        // 并发读取测试
        CountDownLatch readLatch = new CountDownLatch(threadCount);
        long readStartTime = System.nanoTime();
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executorService.submit(() -> {
                try {
                    for (int i = 0; i < operationsPerThread; i++) {
                        String key = "concurrent_write:" + threadId + ":" + i;
                        basicCache.getIfPresent(key);
                    }
                } finally {
                    readLatch.countDown();
                }
            });
        }
        
        try {
            readLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long readEndTime = System.nanoTime();
        long concurrentReadTime = (readEndTime - readStartTime) / 1_000_000;
        
        int totalOperations = threadCount * operationsPerThread;
        CacheStats stats = basicCache.stats();
        
        result.put("threadCount", threadCount);
        result.put("operationsPerThread", operationsPerThread);
        result.put("totalOperations", totalOperations);
        result.put("concurrentWriteTimeMs", concurrentWriteTime);
        result.put("concurrentReadTimeMs", concurrentReadTime);
        result.put("writeOpsPerSecond", totalOperations * 1000L / concurrentWriteTime);
        result.put("readOpsPerSecond", totalOperations * 1000L / concurrentReadTime);
        result.put("hitRate", stats.hitRate());
        result.put("finalCacheSize", basicCache.estimatedSize());
        
        log.info("并发性能测试完成 - 写入: {}ms, 读取: {}ms", concurrentWriteTime, concurrentReadTime);
        return result;
    }

    /**
     * 内存使用测试
     */
    public Map<String, Object> memoryUsageTest() {
        log.info("执行内存使用测试...");
        
        Map<String, Object> result = new HashMap<>();
        
        // 获取初始内存使用情况
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 向权重缓存添加大量数据
        int testSize = 1000;
        weightCache.invalidateAll();
        
        for (int i = 0; i < testSize; i++) {
            UserInfo user = createLargeTestUser(i);
            weightCache.put("weight_test:" + i, user);
        }
        
        // 强制垃圾回收
        System.gc();
        Thread.yield();
        
        // 获取使用后内存情况
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterMemory - initialMemory;
        
        // 缓存统计
        CacheStats stats = weightCache.stats();
        long cacheSize = weightCache.estimatedSize();
        
        result.put("testSize", testSize);
        result.put("initialMemoryMB", initialMemory / (1024 * 1024));
        result.put("afterMemoryMB", afterMemory / (1024 * 1024));
        result.put("memoryUsedMB", memoryUsed / (1024 * 1024));
        result.put("memoryPerItemKB", memoryUsed / testSize / 1024);
        result.put("cacheSize", cacheSize);
        result.put("evictionCount", stats.evictionCount());
        result.put("hitRate", stats.hitRate());
        
        log.info("内存使用测试完成 - 使用内存: {}MB, 缓存大小: {}", memoryUsed / (1024 * 1024), cacheSize);
        return result;
    }

    /**
     * 缓存命中率测试
     */
    public Map<String, Object> hitRateTest() {
        log.info("执行缓存命中率测试...");
        
        Map<String, Object> result = new HashMap<>();
        
        // 清空缓存
        accessCache.invalidateAll();
        
        int totalOperations = 10000;
        int uniqueKeys = 1000; // 使用1000个唯一键，模拟热点数据
        Random random = new Random();
        
        // 执行随机访问，模拟真实场景
        for (int i = 0; i < totalOperations; i++) {
            int keyIndex = random.nextInt(uniqueKeys);
            String key = "hit_test:" + keyIndex;
            
            Object value = accessCache.getIfPresent(key);
            if (value == null) {
                // 模拟从数据源加载
                UserInfo user = createTestUser(keyIndex);
                accessCache.put(key, user);
            }
        }
        
        CacheStats stats = accessCache.stats();
        
        result.put("totalOperations", totalOperations);
        result.put("uniqueKeys", uniqueKeys);
        result.put("hitCount", stats.hitCount());
        result.put("missCount", stats.missCount());
        result.put("hitRate", stats.hitRate());
        result.put("missRate", stats.missRate());
        result.put("requestCount", stats.requestCount());
        result.put("cacheSize", accessCache.estimatedSize());
        result.put("expectedHitRate", "约 " + (int)((totalOperations - uniqueKeys) * 100.0 / totalOperations) + "%");
        
        log.info("缓存命中率测试完成 - 命中率: {:.2%}", stats.hitRate());
        return result;
    }

    /**
     * 创建测试用户
     */
    private UserInfo createTestUser(int id) {
        return UserInfo.builder()
                .id(id)
                .name("TestUser" + id)
                .sex(id % 2 == 0 ? "女" : "男")
                .age(20 + id % 50)
                .email("test" + id + "@example.com")
                .phone("138" + String.format("%08d", id))
                .department("测试部门" + (id % 5))
                .position("测试职位" + (id % 3))
                .salary(5000.0 + id * 10)
                .hireDate(LocalDateTime.now().minusDays(id % 365))
                .lastLoginTime(LocalDateTime.now().minusHours(id % 24))
                .status(UserInfo.UserStatus.ACTIVE)
                .tags(Arrays.asList("tag" + (id % 3), "performance"))
                .permissions(Arrays.asList("read", "test"))
                .build();
    }

    /**
     * 创建大对象测试用户（用于内存测试）
     */
    private UserInfo createLargeTestUser(int id) {
        List<String> largeTags = new ArrayList<>();
        List<String> largePermissions = new ArrayList<>();
        
        // 添加大量标签和权限，增加对象大小
        for (int i = 0; i < 50; i++) {
            largeTags.add("large_tag_" + id + "_" + i);
            largePermissions.add("large_permission_" + id + "_" + i);
        }
        
        return UserInfo.builder()
                .id(id)
                .name("LargeTestUser" + id + "_with_very_long_name_for_memory_testing")
                .sex(id % 2 == 0 ? "女" : "男")
                .age(20 + id % 50)
                .email("large_test_user_" + id + "@very-long-domain-name-for-testing.com")
                .phone("138" + String.format("%08d", id))
                .department("非常长的部门名称用于内存测试_" + (id % 5))
                .position("非常长的职位名称用于内存测试_" + (id % 3))
                .salary(5000.0 + id * 10)
                .hireDate(LocalDateTime.now().minusDays(id % 365))
                .lastLoginTime(LocalDateTime.now().minusHours(id % 24))
                .status(UserInfo.UserStatus.ACTIVE)
                .tags(largeTags)
                .permissions(largePermissions)
                .build();
    }

    /**
     * 获取所有缓存的性能指标
     */
    public Map<String, Object> getAllCacheMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("basicCache", getCacheMetrics(basicCache.stats(), basicCache.estimatedSize()));
        metrics.put("loadingCache", getCacheMetrics(loadingCache.stats(), loadingCache.estimatedSize()));
        metrics.put("accessCache", getCacheMetrics(accessCache.stats(), accessCache.estimatedSize()));
        metrics.put("weightCache", getCacheMetrics(weightCache.stats(), weightCache.estimatedSize()));
        
        return metrics;
    }

    /**
     * 获取单个缓存的指标
     */
    private Map<String, Object> getCacheMetrics(CacheStats stats, long size) {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("size", size);
        metrics.put("hitCount", stats.hitCount());
        metrics.put("missCount", stats.missCount());
        metrics.put("hitRate", stats.hitRate());
        metrics.put("missRate", stats.missRate());
        metrics.put("loadCount", stats.loadCount());
        metrics.put("totalLoadTime", stats.totalLoadTime());
        metrics.put("averageLoadPenalty", stats.averageLoadPenalty() / 1_000_000); // 转换为毫秒
        metrics.put("evictionCount", stats.evictionCount());
        metrics.put("requestCount", stats.requestCount());
        
        return metrics;
    }

    /**
     * 清理资源
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
