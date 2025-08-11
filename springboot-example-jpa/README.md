# Spring Boot JPA 示例模块

该模块演示了如何在Spring Boot应用程序中使用JPA（Java Persistence API）进行数据持久化操作，包含了常见的CRUD操作和面试中常考的API使用。

## 功能特性

- 完整的用户实体CRUD操作
- 常用的JPA查询方法示例
- 分页查询和排序功能
- 自定义JPQL查询和原生SQL查询
- 事务管理和批量操作
- Swagger API文档支持

## 主要组件

1. `User` - 用户实体类，演示JPA基本注解
2. `UserRepository` - 数据访问接口，演示各种查询方法
3. `UserService` - 业务逻辑服务类
4. `UserController` - REST API控制器
5. `SwaggerConfig` - Swagger配置类

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

## 使用方法

1. 确保MySQL数据库服务正在运行，并创建了testdb数据库：
   ```sql
   CREATE DATABASE IF NOT EXISTS testdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 修改数据库连接配置（如需要）：
   在 `src/main/resources/application.yml` 中修改数据库用户名和密码

3. 启动应用：
   ```bash
   cd springboot-example-jpa
   mvn spring-boot:run
   ```

4. 访问Swagger UI（API文档）：
   浏览器访问 `http://localhost:8280/swagger-ui.html`

5. 使用REST API测试功能：
   - 创建用户：`POST http://localhost:8280/users`
   - 查询用户：`GET http://localhost:8280/users`
   - 分页查询：`GET http://localhost:8280/users/page?page=0&size=5`

## 学习要点

1. JPA基本概念和配置
2. 实体映射和生命周期管理
3. Repository接口的使用和扩展
4. 查询方法的定义和使用
5. 分页和排序处理
6. 事务管理
7. 常见面试问题：
   - JPA vs Hibernate vs Spring Data JPA的区别
   - 常用查询方法关键字
   - FetchType.LAZY和FetchType.EAGER的区别
   - @Transactional的使用场景
   - EntityManager的常用方法