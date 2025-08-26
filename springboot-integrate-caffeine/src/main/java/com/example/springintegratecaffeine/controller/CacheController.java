package com.example.springintegratecaffeine.controller;

import com.example.springintegratecaffeine.service.CacheService;
import com.example.springintegratecaffeine.service.PerformanceTestService;
import com.example.springintegratecaffeine.service.CacheMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存演示控制器
 * 提供各种缓存操作的 REST API
 */
@Slf4j
@RestController
@RequestMapping("/api/cache")
@Tag(name = "缓存演示", description = "Caffeine 缓存各种功能的演示接口")
public class CacheController {

    @Resource
    private CacheService cacheService;

    @Resource
    private PerformanceTestService performanceTestService;

    @Resource
    private CacheMonitoringService cacheMonitoringService;

    @Operation(summary = "基础缓存演示", description = "演示基本的缓存存取操作")
    @PostMapping("/demo/basic")
    public ResponseEntity<String> basicCacheDemo() {
        try {
            cacheService.basicCacheDemo();
            return ResponseEntity.ok("基础缓存演示完成，请查看日志");
        } catch (Exception e) {
            log.error("基础缓存演示失败", e);
            return ResponseEntity.internalServerError().body("演示失败: " + e.getMessage());
        }
    }

    @Operation(summary = "自动加载缓存演示", description = "演示缓存穿透保护和自动加载功能")
    @PostMapping("/demo/loading")
    public ResponseEntity<String> loadingCacheDemo() {
        try {
            cacheService.loadingCacheDemo();
            return ResponseEntity.ok("自动加载缓存演示完成，请查看日志");
        } catch (Exception e) {
            log.error("自动加载缓存演示失败", e);
            return ResponseEntity.internalServerError().body("演示失败: " + e.getMessage());
        }
    }

    @Operation(summary = "访问时间缓存演示", description = "演示基于访问时间的缓存过期策略")
    @PostMapping("/demo/access")
    public ResponseEntity<String> accessCacheDemo() {
        try {
            cacheService.accessCacheDemo();
            return ResponseEntity.ok("访问时间缓存演示完成，请查看日志");
        } catch (Exception e) {
            log.error("访问时间缓存演示失败", e);
            return ResponseEntity.internalServerError().body("演示失败: " + e.getMessage());
        }
    }

    @Operation(summary = "权重缓存演示", description = "演示基于对象大小的缓存管理")
    @PostMapping("/demo/weight")
    public ResponseEntity<String> weightCacheDemo() {
        try {
            cacheService.weightCacheDemo();
            return ResponseEntity.ok("权重缓存演示完成，请查看日志");
        } catch (Exception e) {
            log.error("权重缓存演示失败", e);
            return ResponseEntity.internalServerError().body("演示失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取缓存统计信息", description = "获取所有缓存的详细统计信息")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCacheStatistics() {
        try {
            Map<String, Object> stats = cacheService.getCacheStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取缓存统计信息失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "缓存健康检查", description = "检查所有缓存的健康状态")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            Map<String, Object> health = cacheService.getCacheHealthStatus();
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            log.error("缓存健康检查失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "综合性能测试", description = "执行全面的缓存性能测试")
    @PostMapping("/performance/comprehensive")
    public ResponseEntity<Map<String, Object>> comprehensivePerformanceTest() {
        try {
            Map<String, Object> results = performanceTestService.comprehensivePerformanceTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("综合性能测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "基础缓存性能测试", description = "测试基础缓存的读写性能")
    @PostMapping("/performance/basic")
    public ResponseEntity<Map<String, Object>> basicCachePerformanceTest() {
        try {
            Map<String, Object> results = performanceTestService.basicCachePerformanceTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("基础缓存性能测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "自动加载缓存性能测试", description = "测试自动加载缓存的性能")
    @PostMapping("/performance/loading")
    public ResponseEntity<Map<String, Object>> loadingCachePerformanceTest() {
        try {
            Map<String, Object> results = performanceTestService.loadingCachePerformanceTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("自动加载缓存性能测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "并发性能测试", description = "测试缓存在并发场景下的性能")
    @PostMapping("/performance/concurrency")
    public ResponseEntity<Map<String, Object>> concurrencyPerformanceTest() {
        try {
            Map<String, Object> results = performanceTestService.concurrencyPerformanceTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("并发性能测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "内存使用测试", description = "测试缓存的内存使用情况")
    @PostMapping("/performance/memory")
    public ResponseEntity<Map<String, Object>> memoryUsageTest() {
        try {
            Map<String, Object> results = performanceTestService.memoryUsageTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("内存使用测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "缓存命中率测试", description = "测试缓存的命中率表现")
    @PostMapping("/performance/hit-rate")
    public ResponseEntity<Map<String, Object>> hitRateTest() {
        try {
            Map<String, Object> results = performanceTestService.hitRateTest();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("缓存命中率测试失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "实时监控数据", description = "获取所有缓存的实时监控指标")
    @GetMapping("/monitoring/realtime")
    public ResponseEntity<Map<String, Object>> getRealTimeMetrics() {
        try {
            Map<String, Object> metrics = cacheMonitoringService.getRealTimeMetrics();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            log.error("获取实时监控数据失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "历史监控数据", description = "获取缓存的历史监控数据")
    @GetMapping("/monitoring/history")
    public ResponseEntity<Map<String, Object>> getHistoricalMetrics() {
        try {
            Map<String, Object> metrics = new HashMap<>();
            metrics.put("history", cacheMonitoringService.getHistoricalMetrics());
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            log.error("获取历史监控数据失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "缓存健康状态", description = "获取所有缓存的健康状态")
    @GetMapping("/monitoring/health")
    public ResponseEntity<Map<String, Object>> getCacheHealthStatus() {
        try {
            Map<String, Object> health = cacheMonitoringService.getCacheHealthStatus();
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            log.error("获取缓存健康状态失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "性能指标", description = "获取所有缓存的性能指标")
    @GetMapping("/monitoring/metrics")
    public ResponseEntity<Map<String, Object>> getAllCacheMetrics() {
        try {
            Map<String, Object> metrics = performanceTestService.getAllCacheMetrics();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            log.error("获取性能指标失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "缓存性能测试", description = "测试缓存的读写性能")
    @PostMapping("/performance-test")
    public ResponseEntity<Map<String, Object>> performanceTest(
            @Parameter(description = "测试迭代次数", example = "10000")
            @RequestParam(defaultValue = "10000") int iterations) {
        try {
            if (iterations <= 0 || iterations > 100000) {
                return ResponseEntity.badRequest().build();
            }
            
            Map<String, Object> results = cacheService.performanceTest(iterations);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("缓存性能测试失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "运行所有演示", description = "依次运行所有缓存演示")
    @PostMapping("/demo/all")
    public ResponseEntity<Map<String, Object>> runAllDemos() {
        try {
            log.info("开始运行所有缓存演示...");
            
            cacheService.basicCacheDemo();
            Thread.sleep(1000);
            
            cacheService.loadingCacheDemo();
            Thread.sleep(1000);
            
            cacheService.accessCacheDemo();
            Thread.sleep(1000);
            
            cacheService.weightCacheDemo();
            Thread.sleep(1000);
            
            // 获取最终统计信息
            Map<String, Object> finalStats = cacheService.getCacheStatistics();
            
            log.info("所有缓存演示完成");
            Map<String, Object> response = new HashMap<>();
            response.put("message", "所有演示完成");
            response.put("statistics", finalStats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("运行所有演示失败", e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "演示失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorMap);
        }
    }

    @Operation(summary = "清理所有缓存", description = "清空所有缓存中的数据")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllCaches() {
        try {
            cacheService.clearAllCaches();
            return ResponseEntity.ok("所有缓存已清理");
        } catch (Exception e) {
            log.error("清理缓存失败", e);
            return ResponseEntity.internalServerError().body("清理失败: " + e.getMessage());
        }
    }

}
