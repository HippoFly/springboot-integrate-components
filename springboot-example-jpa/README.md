# Spring Boot JPA 完整示例模块

该模块是一个完整的 Spring Boot JPA 示例项目，展示了 JPA 的各种高级特性和最佳实践，包含了企业级开发中常用的功能和面试中常考的知识点。

## 功能特性

### 基础功能
- 完整的实体 CRUD 操作（用户、部门、项目）
- 常用的 JPA 查询方法示例
- 分页查询和排序功能
- 自定义 JPQL 查询和原生 SQL 查询
- 事务管理和批量操作
- Swagger API 文档支持

### 高级特性
- **继承映射**：演示 JPA 继承策略（Employee、FullTimeEmployee、PartTimeEmployee）
- **复合主键**：演示 @EmbeddedId 的使用（UserRole）
- **审计功能**：自动记录创建时间、更新时间、操作人等审计信息
- **二级缓存**：使用 EhCache 实现实体和查询缓存
- **动态查询**：Criteria API 和 Specification 的使用
- **关系映射**：一对多、多对多、多对一关系的完整演示
- **乐观锁**：使用 @Version 实现乐观锁控制
- **生命周期回调**：@PrePersist、@PreUpdate 等回调方法

## 主要组件

### 核心实体类
1. **`User`** - 用户实体，演示基础 JPA 注解和关系映射
2. **`Department`** - 部门实体，演示一对多关系
3. **`Project`** - 项目实体，演示多对多关系
4. **`Employee`** - 员工抽象类，演示继承映射
5. **`FullTimeEmployee`** / **`PartTimeEmployee`** - 具体员工类，演示继承策略
6. **`Role`** - 角色实体，演示枚举和状态管理
7. **`UserRole`** - 用户角色关联，演示复合主键
8. **`BaseAuditEntity`** - 审计基类，演示审计功能

### 数据访问层
1. **`UserRepository`** - 用户数据访问，演示各种查询方法
2. **`DepartmentRepository`** - 部门数据访问
3. **`ProjectRepository`** - 项目数据访问
4. **`EmployeeRepository`** - 员工数据访问，演示继承查询
5. **`RoleRepository`** - 角色数据访问
6. **`UserRoleRepository`** - 复合主键实体数据访问

### 业务服务层
1. **`UserService`** - 用户业务逻辑服务
2. **`UserQueryService`** - 高级查询服务，演示 Criteria API 和 Specification
3. **`TransactionService`** - 事务管理服务，演示各种事务特性

### 控制器层
1. **`UserController`** - 用户基础 CRUD API
2. **`UserQueryController`** - 高级查询 API
3. **`DepartmentController`** - 部门管理 API
4. **`ProjectController`** - 项目管理 API

### 配置类
1. **`SwaggerConfig`** - OpenAPI 3.0 配置
2. **`JpaAuditingConfig`** - JPA 审计配置
3. **`CacheConfig`** - 缓存配置
4. **`DataInitializer`** - 数据初始化器

## 技术要点

### 实体类注解
- `@Entity` - 标识为JPA实体
- `@Table` - 指定表名
- `@Id` - 主键标识
- `@GeneratedValue` - 主键生成策略
- `@Column` - 字段映射配置
- `@PrePersist` 和 `@PreUpdate` - 生命周期回调

### Repository查询方法
1. **方法名查询**：
   - findByUsername(String username)
   - findByUsernameAndEmail(String username, String email)
   - findByAgeBetween(Integer minAge, Integer maxAge)

2. **@Query注解查询**：
   - JPQL查询
   - 原生SQL查询
   - 更新操作(@Modifying)

3. **分页和排序**：
   - Pageable接口使用
   - Sort对象使用

### 常用API和面试考点

1. **JpaRepository接口**：
   - save() - 保存实体
   - findById() - 根据ID查找
   - findAll() - 查找所有
   - deleteById() - 根据ID删除
   - count() - 统计数量

2. **查询方法关键字**：
   - And/Or
   - Between/In/NotIn
   - Like/Containing
   - GreaterThan/LessThan
   - OrderBy

3. **事务管理**：
   - @Transactional注解
   - @Modifying更新操作

## API接口

### 用户管理接口

- `POST /users` - 创建用户
- `POST /users/batch` - 批量创建用户
- `GET /users/{id}` - 根据ID获取用户
- `GET /users` - 获取所有用户
- `GET /users/page` - 分页获取用户
- `GET /users/username/{username}` - 根据用户名获取用户
- `GET /users/search?username={username}` - 模糊查询用户
- `GET /users/age?minAge={min}&maxAge={max}` - 根据年龄范围查询用户
- `PUT /users/{username}/email?email={email}` - 更新用户邮箱
- `DELETE /users/{id}` - 删除用户
- `DELETE /users` - 删除所有用户
- `GET /users/count` - 获取用户总数

## 快速开始

### 环境准备
1. **Java 8+** 和 **Maven 3.6+**
2. **MySQL 5.7+** 或使用内置的 **H2 数据库**（测试用）

### 启动步骤

#### 方式一：使用 MySQL 数据库
1. 确保 MySQL 服务运行，创建数据库：
   ```sql
   CREATE DATABASE IF NOT EXISTS testdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 修改数据库配置（可选）：
   编辑 `src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
       username: your_username
       password: your_password
   ```

#### 方式二：使用 H2 内存数据库（推荐学习使用）
修改 `application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  h2:
    console:
      enabled: true
      path: /h2-console
```

### 启动应用
```bash
cd springboot-example-jpa
mvn clean spring-boot:run
```

### 访问应用
- **Swagger API 文档**：http://localhost:8280/swagger-ui/index.html
- **H2 控制台**（如使用 H2）：http://localhost:8280/h2-console

## 学习路径和步骤

### 第一步：基础概念理解
1. **启动项目**，观察控制台输出的数据初始化过程
2. **访问 Swagger UI**：http://localhost:8280/swagger-ui/index.html
3. **了解项目结构**：
   - 实体类（`entity` 包）
   - 数据访问层（`repository` 包）
   - 业务服务层（`service` 包）
   - 控制器层（`controller` 包）

### 第二步：基础 CRUD 操作
使用 Swagger UI 或 curl 命令测试基础功能：

```bash
# 1. 查看所有用户
curl -X GET "http://localhost:8280/users"

# 2. 创建新用户
curl -X POST "http://localhost:8280/users" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "age": 28
  }'

# 3. 分页查询用户
curl -X GET "http://localhost:8280/users/page?page=0&size=5&sortBy=age&sortDir=desc"

# 4. 根据年龄范围查询
curl -X GET "http://localhost:8280/users/age?minAge=25&maxAge=35"
```

### 第三步：高级查询功能
测试动态查询和 Criteria API：

```bash
# 1. 动态查询（Specification）
curl -X GET "http://localhost:8280/users/query/dynamic?username=test&minAge=20&maxAge=40&page=0&size=10"

# 2. 统计查询
curl -X GET "http://localhost:8280/users/query/statistics?minAge=25"

# 3. 部门统计
curl -X GET "http://localhost:8280/users/query/department-statistics"

# 4. 查找参与项目最多的用户
curl -X GET "http://localhost:8280/users/query/most-projects"
```

### 第四步：关系映射学习
1. **一对多关系**：查看 Department 和 User 的关系
2. **多对多关系**：查看 User 和 Project 的关系
3. **复合主键**：查看 UserRole 的实现

```bash
# 查看部门及其用户
curl -X GET "http://localhost:8280/departments/1/users"

# 查看用户参与的项目
curl -X GET "http://localhost:8280/users/1/projects"
```

### 第五步：高级特性学习
1. **继承映射**：查看 Employee、FullTimeEmployee、PartTimeEmployee
2. **审计功能**：观察实体的创建时间、更新时间自动填充
3. **缓存机制**：多次查询同一数据，观察性能提升
4. **事务管理**：查看 TransactionService 的各种事务场景

### 第六步：测试和调试
1. **运行单元测试**：
   ```bash
   mvn test
   ```
2. **查看测试覆盖率**
3. **使用 H2 控制台**查看数据库结构和数据

## API 接口完整列表

### 用户管理 API
- `POST /users` - 创建用户
- `POST /users/batch` - 批量创建用户
- `GET /users/{id}` - 根据ID获取用户
- `GET /users` - 获取所有用户
- `GET /users/page` - 分页获取用户
- `GET /users/username/{username}` - 根据用户名获取用户
- `GET /users/search?username={username}` - 模糊查询用户
- `GET /users/age?minAge={min}&maxAge={max}` - 根据年龄范围查询用户
- `PUT /users/{userId}/projects/{projectId}` - 为用户分配项目
- `GET /users/{userId}/projects` - 获取用户参与的项目
- `PUT /users/email?username={username}&email={email}` - 更新用户邮箱
- `DELETE /users/{id}` - 删除用户
- `DELETE /users` - 删除所有用户
- `GET /users/count` - 获取用户总数

### 高级查询 API
- `GET /users/query/dynamic` - 动态查询用户（支持多条件组合）
- `GET /users/query/statistics` - 用户统计查询
- `GET /users/query/most-projects` - 查找参与项目最多的用户
- `GET /users/query/by-department-age` - 按部门和年龄查询用户
- `GET /users/query/department-statistics` - 部门统计信息

### 部门管理 API
- `POST /departments` - 创建部门
- `GET /departments/{id}` - 根据ID获取部门
- `GET /departments` - 获取所有部门
- `GET /departments/page` - 分页获取部门
- `GET /departments/name/{name}` - 根据名称获取部门
- `GET /departments/search?name={name}` - 模糊查询部门
- `PUT /departments/{departmentId}/users/{userId}` - 为部门分配用户
- `GET /departments/{id}/users` - 获取部门下的所有用户
- `DELETE /departments/{id}` - 删除部门
- `GET /departments/count` - 获取部门总数

### 项目管理 API
- `POST /projects` - 创建项目
- `GET /projects/{id}` - 根据ID获取项目
- `GET /projects` - 获取所有项目
- `GET /projects/page` - 分页获取项目
- `DELETE /projects/{id}` - 删除项目

## 学习要点和面试考点

### 1. JPA 基础概念
- **JPA vs Hibernate vs Spring Data JPA** 的区别
- **实体生命周期**：Transient、Persistent、Detached、Removed
- **主键生成策略**：IDENTITY、SEQUENCE、TABLE、AUTO

### 2. 实体映射注解
- `@Entity`、`@Table`、`@Id`、`@GeneratedValue`
- `@Column`、`@Temporal`、`@Enumerated`
- `@Version`（乐观锁）、`@CreatedDate`、`@LastModifiedDate`

### 3. 关系映射
- **一对一**：`@OneToOne`
- **一对多**：`@OneToMany`、`@JoinColumn`
- **多对一**：`@ManyToOne`
- **多对多**：`@ManyToMany`、`@JoinTable`
- **级联操作**：CascadeType 的使用
- **懒加载 vs 立即加载**：FetchType.LAZY vs FetchType.EAGER

### 4. 查询方法
- **方法名查询**：findBy、countBy、deleteBy 等关键字
- **@Query 注解**：JPQL 和原生 SQL
- **@Modifying**：更新和删除操作
- **分页和排序**：Pageable、Sort 的使用

### 5. 高级查询
- **Criteria API**：类型安全的动态查询
- **Specification**：查询条件的组合和复用
- **子查询**、**连接查询**、**聚合查询**

### 6. 事务管理
- **@Transactional** 注解的使用
- **事务传播行为**：REQUIRED、REQUIRES_NEW、NESTED 等
- **事务隔离级别**：READ_COMMITTED、SERIALIZABLE 等
- **事务回滚**：rollbackFor 属性

### 7. 性能优化
- **二级缓存**：实体缓存和查询缓存
- **N+1 问题**的解决方案
- **批量操作**的最佳实践
- **连接池**配置

### 8. 高级特性
- **继承映射**：TABLE_PER_CLASS、SINGLE_TABLE、JOINED
- **复合主键**：@EmbeddedId、@IdClass
- **审计功能**：@CreatedBy、@LastModifiedBy
- **生命周期回调**：@PrePersist、@PostLoad 等