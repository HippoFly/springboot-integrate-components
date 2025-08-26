# Spring Boot 集成 Caffeine 缓存完整指南

本项目提供了 Spring Boot 集成 Caffeine 缓存的完整学习和实践指南，包含多种缓存策略、Spring Cache 注解使用、性能监控等全面功能。

## 📋 目录

- [项目概述](#项目概述)
- [快速开始](#快速开始)
- [核心功能](#核心功能)
- [学习路径](#学习路径)
- [API 接口文档](#api-接口文档)
- [最佳实践](#最佳实践)
- [性能监控](#性能监控)
- [常见问题](#常见问题)

## 🎯 项目概述

Caffeine 是一个基于 Java 8 的高性能本地缓存库，提供了接近最优的命中率。本项目展示了：

### 核心特性
- **多种缓存策略**：基础缓存、自动加载缓存、访问时间过期、权重缓存
- **Spring Cache 集成**：完整的声明式缓存注解使用
- **性能监控**：缓存统计、性能测试、监控指标
- **实际应用场景**：用户信息缓存、数据预热、批量操作

### 技术栈
- Spring Boot 2.7.0
- Caffeine 3.1.1
- Spring Cache Abstraction
- SpringDoc OpenAPI 3
- Spring Boot Actuator + Micrometer

## 🚀 快速开始

### 1. 环境要求
- JDK 8+
- Maven 3.6+

### 2. 启动项目
```bash
# 克隆项目
git clone <repository-url>
cd springboot-integrate-caffeine

# 编译运行
mvn clean compile
mvn spring-boot:run
```

### 3. 访问应用
- **应用地址**: http://localhost:8080
- **API 文档**: http://localhost:8080/swagger-ui.html
- **监控端点**: http://localhost:8080/actuator

### 4. 快速测试
```bash
# 基础缓存演示
curl -X POST http://localhost:8080/cache/demo/basic

# 查看缓存统计
curl http://localhost:8080/cache/stats

# 性能测试
curl -X POST http://localhost:8080/cache/performance-test
```

## 🔧 核心功能

### 1. 缓存配置 (`CacheConfig.java`)

#### 基础手动缓存
```java
@Bean("basicCache")
public Cache<String, Object> basicCache() {
    return Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)    // 写入后5分钟过期
            .initialCapacity(100)                     // 初始容量
            .maximumSize(1000)                        // 最大条目数
            .recordStats()                            // 启用统计
            .build();
}
```

#### 自动加载缓存
```java
@Bean("loadingCache")
public LoadingCache<String, String> loadingCache() {
    return Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(500)
            .recordStats()
            .build(key -> "Loaded value for " + key); // 自动加载函数
}
```

#### 访问时间过期缓存
```java
@Bean("accessCache")
public Cache<String, Object> accessCache() {
    return Caffeine.newBuilder()
            .expireAfterAccess(3, TimeUnit.MINUTES)   // 访问后3分钟过期
            .maximumSize(200)
            .recordStats()
            .build();
}
```

#### 权重缓存
```java
@Bean("weightCache")
public Cache<String, UserInfo> weightCache() {
    return Caffeine.newBuilder()
            .maximumWeight(10000)                     // 最大权重
            .weigher((String key, UserInfo value) -> value.calculateSize()) // 权重计算
            .recordStats()
            .build();
}
```

### 2. Spring Cache 注解使用 (`AutoUserInfoServiceImpl.java`)

#### @Cacheable - 缓存查询结果
```java
@Cacheable(value = "userCache", key = "#id", condition = "#id > 0")
public UserInfo findById(Integer id) {
    // 模拟数据库查询
    return database.get(id);
}
```

#### @CachePut - 更新缓存
```java
@CachePut(value = "userCache", key = "#userInfo.id")
public UserInfo updateUser(UserInfo userInfo) {
    database.put(userInfo.getId(), userInfo);
    return userInfo;
}
```

#### @CacheEvict - 清除缓存
```java
@CacheEvict(value = "userCache", key = "#id")
public void deleteById(Integer id) {
    database.remove(id);
}
```

#### @Caching - 复合缓存操作
```java
@Caching(
    cacheable = @Cacheable(value = "userCache", key = "#id"),
    put = @CachePut(value = "shortTermCache", key = "#id")
)
public UserInfo findByIdWithMultiCache(Integer id) {
    return database.get(id);
}
```

### 3. 缓存服务 (`CacheService.java`)

提供各种缓存操作演示：
- 基础缓存 CRUD 操作
- 自动加载缓存使用
- 访问时间过期演示
- 权重缓存管理
- 缓存统计信息
- 性能测试方法

## 📚 学习路径

### 第一步：理解缓存基础概念
1. **启动项目**，访问 Swagger UI
2. **执行基础缓存演示**：`POST /cache/demo/basic`
3. **查看缓存统计**：`GET /cache/stats`
4. **理解缓存的存储和检索机制**

### 第二步：学习自动加载缓存
1. **执行自动加载演示**：`POST /cache/demo/loading`
2. **观察自动加载机制**：缓存未命中时自动调用加载函数
3. **测试批量获取**：`loadingCache.getAll()`

### 第三步：掌握过期策略
1. **访问时间过期**：`POST /cache/demo/access`
   - 理解 `expireAfterAccess` 的工作原理
   - 观察缓存访问如何重置过期时间
2. **写入时间过期**：`expireAfterWrite`
   - 固定时间后过期，不受访问影响

### 第四步：权重缓存和内存管理
1. **权重缓存演示**：`POST /cache/demo/weight`
2. **理解权重计算**：基于对象大小的内存管理
3. **观察缓存淘汰**：权重超限时的淘汰策略

### 第五步：Spring Cache 注解
1. **查看用户服务实现**：`AutoUserInfoServiceImpl.java`
2. **测试声明式缓存**：
   ```bash
   # 创建用户（缓存）
   curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json" \
     -d '{"name":"张三","age":25}'
   
   # 查询用户（从缓存）
   curl http://localhost:8080/users/1
   
   # 更新用户（更新缓存）
   curl -X PUT http://localhost:8080/users/1 \
     -H "Content-Type: application/json" \
     -d '{"id":1,"name":"李四","age":26}'
   ```

### 第六步：性能测试和监控
1. **执行性能测试**：`POST /cache/performance-test`
2. **分析测试结果**：命中率、平均加载时间、吞吐量
3. **监控缓存指标**：通过 Actuator 端点

### 第七步：缓存管理操作
1. **缓存预热**：`POST /cache/warmup`
2. **清除缓存**：`DELETE /cache/clear`
3. **健康检查**：`GET /cache/health`

## 📖 API 接口文档

### 缓存演示接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/cache/demo/basic` | POST | 基础缓存操作演示 |
| `/cache/demo/loading` | POST | 自动加载缓存演示 |
| `/cache/demo/access` | POST | 访问时间过期缓存演示 |
| `/cache/demo/weight` | POST | 权重缓存演示 |

### 缓存管理接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/cache/stats` | GET | 获取所有缓存统计信息 |
| `/cache/performance-test` | POST | 执行缓存性能测试 |
| `/cache/warmup` | POST | 缓存预热 |
| `/cache/clear` | DELETE | 清除所有缓存 |
| `/cache/health` | GET | 缓存健康检查 |

### 用户服务接口（Spring Cache 演示）

| 接口 | 方法 | 描述 |
|------|------|------|
| `/users` | POST | 创建用户 |
| `/users/{id}` | GET | 查询用户 |
| `/users/{id}` | PUT | 更新用户 |
| `/users/{id}` | DELETE | 删除用户 |

## 🎯 最佳实践

### 1. 缓存策略选择
- **基础缓存**：适用于手动控制缓存生命周期的场景
- **自动加载缓存**：适用于数据源稳定、加载逻辑简单的场景
- **访问时间过期**：适用于热点数据缓存
- **权重缓存**：适用于内存敏感的大对象缓存

### 2. 过期策略配置
```java
// 组合使用多种过期策略
Caffeine.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)    // 写入后过期
    .expireAfterAccess(5, TimeUnit.MINUTES)    // 访问后过期
    .refreshAfterWrite(2, TimeUnit.MINUTES)    // 写入后刷新
```

### 3. 缓存键设计
```java
// 使用有意义的键前缀
@Cacheable(value = "userCache", key = "'user:' + #id")

// 复合键设计
@Cacheable(value = "queryCache", key = "#dept + ':' + #status")
```

### 4. 缓存条件控制
```java
// 条件缓存
@Cacheable(value = "userCache", condition = "#id > 0 && #id < 10000")

// 排除条件
@Cacheable(value = "userCache", unless = "#result == null")
```

### 5. 异常处理
```java
try {
    return cache.get(key, k -> loadFromDatabase(k));
} catch (Exception e) {
    log.error("缓存加载失败", e);
    return loadFromDatabase(key); // 降级到直接查询
}
```

## 📊 性能监控

### 1. 缓存统计指标
- **命中率**：`hitRate()`
- **未命中率**：`missRate()`
- **加载次数**：`loadCount()`
- **平均加载时间**：`averageLoadPenalty()`
- **淘汰次数**：`evictionCount()`

### 2. 监控端点
```bash
# Actuator 缓存指标
curl http://localhost:8080/actuator/metrics/cache.gets
curl http://localhost:8080/actuator/metrics/cache.puts
curl http://localhost:8080/actuator/metrics/cache.evictions
```

### 3. 自定义监控
```java
@Scheduled(fixedRate = 60000) // 每分钟输出统计
public void logCacheStats() {
    CacheStats stats = cache.stats();
    log.info("缓存统计 - 命中率: {}, 未命中: {}, 淘汰: {}", 
        stats.hitRate(), stats.missCount(), stats.evictionCount());
}
```

## ❓ 常见问题

### Q1: 如何选择合适的缓存大小？
**A**: 根据应用内存和数据特征：
- 小对象：可设置较大的 `maximumSize`
- 大对象：使用 `maximumWeight` 控制内存使用
- 监控 `evictionCount` 调整大小

### Q2: 缓存穿透如何处理？
**A**: 使用自动加载缓存的空值缓存：
```java
.build(key -> {
    Object value = loadFromDatabase(key);
    return value != null ? value : NULL_OBJECT; // 缓存空对象
})
```

### Q3: 如何实现缓存预热？
**A**: 应用启动时批量加载热点数据：
```java
@PostConstruct
public void warmupCache() {
    List<String> hotKeys = getHotKeys();
    cache.getAll(hotKeys); // 批量预热
}
```

### Q4: Spring Cache 注解不生效？
**A**: 检查以下配置：
- 确保 `@EnableCaching` 注解存在
- 方法必须是 public
- 不能在同一个类内部调用
- 检查 AOP 代理配置

### Q5: 如何处理缓存雪崩？
**A**: 设置随机过期时间：
```java
.expireAfterWrite(Duration.ofMinutes(5 + random.nextInt(5)))
```

## 🔍 学习要点总结

1. **缓存策略理解**：掌握不同缓存类型的适用场景
2. **Spring Cache 集成**：熟练使用声明式缓存注解
3. **性能优化**：通过统计数据优化缓存配置
4. **内存管理**：合理设置缓存大小和权重
5. **监控运维**：建立完善的缓存监控体系

## 📝 扩展学习

- [Caffeine 官方文档](https://github.com/ben-manes/caffeine)
- [Spring Cache 参考文档](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)
- [缓存模式和最佳实践](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.caching)

---

**项目作者**: HippoFly  
**更新时间**: 2025-08-26  
**版本**: 1.0.0