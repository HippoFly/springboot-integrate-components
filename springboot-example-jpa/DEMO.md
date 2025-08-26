# Spring Boot JPA 项目演示指南

## 项目概述

这是一个全面的 Spring Boot JPA 示例项目，展示了 JPA 的各种高级特性和最佳实践。项目包含了从基础的 CRUD 操作到复杂的查询、缓存、事务管理等完整功能。

## 核心功能特性

### 🏗️ 实体设计
- **基础实体**：User、Department、Project
- **继承映射**：Employee（抽象类）→ FullTimeEmployee、PartTimeEmployee
- **复合主键**：UserRole 使用 @EmbeddedId
- **审计功能**：BaseAuditEntity 自动记录创建/更新时间和操作人
- **生命周期回调**：@PrePersist、@PostLoad 等

### 🔗 关系映射
- **一对多**：Department ↔ User
- **多对多**：User ↔ Project
- **复合主键关联**：User ↔ Role（通过 UserRole）
- **继承关系**：Employee 继承映射

### 📊 查询功能
- **方法名查询**：findByUsername、findByAgeBetween 等
- **@Query 注解**：JPQL 和原生 SQL
- **动态查询**：Specification 模式
- **Criteria API**：类型安全的复杂查询
- **分页排序**：Pageable 支持
- **统计查询**：聚合函数、分组查询

### ⚡ 性能优化
- **二级缓存**：Hibernate + EhCache
- **查询缓存**：提升重复查询性能
- **懒加载**：优化数据加载策略
- **批量操作**：减少数据库交互

### 🔄 事务管理
- **声明式事务**：@Transactional
- **事务传播**：REQUIRED、REQUIRES_NEW、NESTED 等
- **事务隔离**：READ_COMMITTED、SERIALIZABLE 等
- **事务回滚**：异常处理和回滚策略

### 🛡️ 异常处理
- **全局异常处理器**：统一处理 JPA 异常
- **自定义业务异常**：BusinessException
- **数据验证**：Bean Validation 集成

## 快速演示

### 1. 项目启动

```bash
# 克隆项目
git clone <repository-url>
cd springboot-example-jpa

# 使用 MySQL（推荐生产环境）
# 1. 创建数据库
CREATE DATABASE testdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 启动应用
mvn spring-boot:run

# 使用 H2 内存数据库（学习测试）
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

### 2. 访问应用

- **Swagger API 文档**：http://localhost:8280/swagger-ui/index.html
- **H2 控制台**（如使用 H2）：http://localhost:8280/h2-console

### 3. 数据初始化

应用启动时会自动初始化测试数据：
- 5个部门
- 50个用户
- 40个项目
- 5个角色
- 15个员工（10个全职 + 5个兼职）
- 用户角色关联关系

### 4. API 功能演示

#### 基础 CRUD 操作

```bash
# 查看所有用户
curl -X GET "http://localhost:8280/users"

# 创建新用户
curl -X POST "http://localhost:8280/users" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "demo_user",
    "email": "demo@example.com",
    "age": 28
  }'

# 分页查询
curl -X GET "http://localhost:8280/users/page?page=0&size=10&sortBy=age&sortDir=desc"
```

#### 高级查询功能

```bash
# 动态查询（Specification）
curl -X GET "http://localhost:8280/users/query/dynamic?username=test&minAge=25&maxAge=35&page=0&size=10"

# 统计查询
curl -X GET "http://localhost:8280/users/query/statistics?minAge=25"

# 部门统计
curl -X GET "http://localhost:8280/users/query/department-statistics"

# 查找参与项目最多的用户
curl -X GET "http://localhost:8280/users/query/most-projects"
```

#### 关系映射操作

```bash
# 查看部门用户
curl -X GET "http://localhost:8280/departments/1/users"

# 为用户分配项目
curl -X PUT "http://localhost:8280/users/1/projects/1"

# 查看用户参与的项目
curl -X GET "http://localhost:8280/users/1/projects"
```

## 学习重点

### 1. 实体映射注解
```java
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @ManyToMany
    @JoinTable(name = "user_project",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();
}
```

### 2. Repository 接口设计
```java
public interface UserRepository extends JpaRepository<User, Long>, 
                                       JpaSpecificationExecutor<User> {
    // 方法名查询
    Optional<User> findByUsername(String username);
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    // JPQL 查询
    @Query("SELECT u FROM User u WHERE u.email LIKE %:email%")
    List<User> findByEmailContaining(@Param("email") String email);
    
    // 原生 SQL
    @Query(value = "SELECT * FROM user WHERE age > :age", nativeQuery = true)
    List<User> findUsersOlderThan(@Param("age") Integer age);
}
```

### 3. 动态查询 Specification
```java
public class UserSpecification {
    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> 
            username == null ? null : 
            criteriaBuilder.like(root.get("username"), "%" + username + "%");
    }
    
    public static Specification<User> ageBetween(Integer minAge, Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            if (minAge == null && maxAge == null) return null;
            if (minAge == null) return criteriaBuilder.le(root.get("age"), maxAge);
            if (maxAge == null) return criteriaBuilder.ge(root.get("age"), minAge);
            return criteriaBuilder.between(root.get("age"), minAge, maxAge);
        };
    }
}
```

### 4. 事务管理
```java
@Service
@Transactional
public class TransactionService {
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewTransaction() {
        // 新事务
    }
    
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void serializableTransaction() {
        // 串行化隔离级别
    }
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void transactionWithTimeout() {
        // 30秒超时，任何异常都回滚
    }
}
```

## 性能优化要点

### 1. 缓存配置
```xml
<!-- ehcache.xml -->
<cache name="com.example.jpa.entity.User"
       maxElementsInMemory="1000"
       eternal="false"
       timeToLiveSeconds="300"
       timeToIdleSeconds="300"/>
```

### 2. 懒加载优化
```java
// 避免 N+1 问题
@Query("SELECT u FROM User u JOIN FETCH u.department WHERE u.id = :id")
Optional<User> findByIdWithDepartment(@Param("id") Long id);
```

### 3. 批量操作
```java
@Modifying
@Query("UPDATE User u SET u.email = :email WHERE u.id IN :ids")
int updateEmailBatch(@Param("email") String email, @Param("ids") List<Long> ids);
```

## 常见问题解决

### 1. N+1 查询问题
使用 JOIN FETCH 或 @EntityGraph 解决

### 2. 懒加载异常
确保在事务范围内访问懒加载属性

### 3. 缓存失效
合理配置缓存策略和失效时间

### 4. 事务回滚
正确配置 rollbackFor 属性

## 测试验证

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=UserServiceTest
mvn test -Dtest=UserQueryServiceIntegrationTest
```

## 项目结构

```
src/main/java/com/example/jpa/
├── config/          # 配置类
│   ├── JpaAuditingConfig.java
│   ├── CacheConfig.java
│   └── SwaggerConfig.java
├── entity/          # 实体类
│   ├── BaseAuditEntity.java
│   ├── User.java
│   ├── Department.java
│   ├── Project.java
│   ├── Employee.java
│   └── ...
├── repository/      # 数据访问层
├── service/         # 业务服务层
├── controller/      # 控制器层
├── specification/   # 查询规范
└── exception/       # 异常处理
```

## 扩展建议

1. **添加更多实体关系**：一对一映射示例
2. **集成 Redis**：分布式缓存
3. **添加数据库迁移**：Flyway 或 Liquibase
4. **性能监控**：集成 Micrometer
5. **安全认证**：Spring Security 集成
6. **API 版本控制**：RESTful API 版本管理

这个项目为学习和理解 Spring Boot JPA 提供了完整的实践环境，涵盖了从基础到高级的各种特性。
