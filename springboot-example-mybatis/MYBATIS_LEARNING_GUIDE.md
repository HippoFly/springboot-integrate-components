# MyBatis 学习指南

本模块展示了MyBatis框架的各种特性和最佳实践，通过实际的代码示例帮助你深入理解MyBatis的使用方法。

## 📚 目录

1. [项目结构](#项目结构)
2. [核心特性展示](#核心特性展示)
3. [实体类设计](#实体类设计)
4. [Mapper接口设计](#mapper接口设计)
5. [XML配置详解](#xml配置详解)
6. [服务层实现](#服务层实现)
7. [控制器API设计](#控制器api设计)
8. [学习要点总结](#学习要点总结)
9. [最佳实践建议](#最佳实践建议)

## 🏗️ 项目结构

```
springboot-example-mybatis/
├── src/main/java/com/example/mybatis/
│   ├── entity/           # 实体类
│   │   ├── UserEntity.java
│   │   ├── DeptEntity.java
│   │   ├── RoleEntity.java
│   │   └── ProjectEntity.java
│   ├── mapper/           # Mapper接口
│   │   ├── UserMapperXml.java
│   │   └── DepartmentMapper.java
│   ├── service/          # 服务层
│   │   ├── UserService.java
│   │   └── impl/UserServiceImpl.java
│   └── controller/       # 控制器
│       └── UserController.java
└── src/main/resources/
    └── mybatis/mapper/   # XML映射文件
        ├── UserMapperXml.xml
        └── DepartmentMapper.xml
```

## 🎯 核心特性展示

### 1. 动态SQL (Dynamic SQL)

**特性说明**: MyBatis最强大的特性之一，允许根据条件动态构建SQL语句。

**代码示例**:
```xml
<!-- 多条件动态查询 -->
<select id="selectByConditions" resultMap="BaseResultMap">
    SELECT <include refid="Base_Column_List"/> FROM users
    <where>
        <if test="username != null and username != ''">
            AND username LIKE CONCAT('%', #{username}, '%')
        </if>
        <if test="minAge != null">AND age >= #{minAge}</if>
        <if test="maxAge != null">AND age <= #{maxAge}</if>
        <if test="gender != null and gender != ''">AND gender = #{gender}</if>
        <if test="departmentId != null">AND department_id = #{departmentId}</if>
    </where>
    ORDER BY create_time DESC
</select>
```

**学习要点**:
- `<where>` 标签自动处理WHERE关键字和AND/OR连接
- `<if>` 标签进行条件判断
- `<set>` 标签用于UPDATE语句的动态字段设置
- `<foreach>` 标签用于处理集合参数

### 2. 结果映射 (ResultMap)

**特性说明**: 定义数据库字段与Java对象属性的映射关系，支持复杂的关联映射。

**代码示例**:
```xml
<!-- 基础结果映射 -->
<resultMap id="BaseResultMap" type="com.example.mybatis.entity.UserEntity">
    <id column="id" property="id" jdbcType="BIGINT"/>
    <result column="username" property="username" jdbcType="VARCHAR"/>
    <result column="real_name" property="realName" jdbcType="VARCHAR"/>
    <!-- 更多字段映射... -->
</resultMap>

<!-- 一对一关联映射 -->
<resultMap id="UserWithDepartmentResultMap" extends="BaseResultMap">
    <association property="department" javaType="com.example.mybatis.entity.DeptEntity">
        <id column="dept_id" property="id" jdbcType="BIGINT"/>
        <result column="dept_name" property="name" jdbcType="VARCHAR"/>
    </association>
</resultMap>
```

**学习要点**:
- `<id>` 标签映射主键字段
- `<result>` 标签映射普通字段
- `<association>` 标签处理一对一关联
- `<collection>` 标签处理一对多关联
- `extends` 属性实现ResultMap继承

### 3. 关联查询 (Association Queries)

**特性说明**: MyBatis支持一对一、一对多、多对多的关联查询。

**一对一关联示例**:
```xml
<select id="selectWithDepartment" resultMap="UserWithDepartmentResultMap">
    SELECT u.*, d.id as dept_id, d.name as dept_name
    FROM users u
    LEFT JOIN departments d ON u.department_id = d.id
    WHERE u.id = #{id}
</select>
```

**一对多关联示例**:
```xml
<resultMap id="DepartmentWithUsersResultMap" extends="BaseResultMap">
    <collection property="users" ofType="com.example.mybatis.entity.UserEntity">
        <id column="user_id" property="id"/>
        <result column="username" property="username"/>
    </collection>
</resultMap>
```

### 4. 分页查询 (Pagination)

**特性说明**: 通过LIMIT和OFFSET实现数据库层面的分页。

**代码示例**:
```xml
<select id="selectWithPagination" resultMap="BaseResultMap">
    SELECT <include refid="Base_Column_List"/> FROM users
    ORDER BY create_time DESC
    LIMIT #{offset}, #{limit}
</select>
```

**Java实现**:
```java
@Override
public Page<UserEntity> findWithPagination(Pageable pageable) {
    int offset = (int) pageable.getOffset();
    int limit = pageable.getPageSize();
    
    List<UserEntity> users = userMapper.selectWithPagination(offset, limit);
    long total = userMapper.countTotal();
    
    return new PageImpl<>(users, pageable, total);
}
```

### 5. 批量操作 (Batch Operations)

**特性说明**: 高效处理大量数据的插入、更新、删除操作。

**批量插入示例**:
```xml
<insert id="batchInsert" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO users (username, real_name, email, age, gender, phone, department_id,
                      create_time, update_time, created_by, updated_by, version)
    VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.realName}, #{user.email}, #{user.age}, 
         #{user.gender}, #{user.phone}, #{user.departmentId}, #{user.createTime}, 
         #{user.updateTime}, #{user.createdBy}, #{user.updatedBy}, #{user.version})
    </foreach>
</insert>
```

**批量删除示例**:
```xml
<delete id="batchDelete">
    DELETE FROM users WHERE id IN
    <foreach collection="ids" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</delete>
```

### 6. SQL片段复用 (SQL Fragments)

**特性说明**: 通过`<sql>`标签定义可重用的SQL片段，提高代码复用性。

**代码示例**:
```xml
<!-- 定义SQL片段 -->
<sql id="Base_Column_List">
    id, username, real_name, email, age, gender, phone, department_id,
    create_time, update_time, created_by, updated_by, version
</sql>

<!-- 使用SQL片段 -->
<select id="selectById" resultMap="BaseResultMap">
    SELECT <include refid="Base_Column_List"/> FROM users WHERE id = #{id}
</select>
```

### 7. 参数处理 (Parameter Handling)

**特性说明**: MyBatis提供多种参数传递方式。

**单个参数**:
```java
UserEntity selectById(@Param("id") Long id);
```

**多个参数**:
```java
List<UserEntity> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
```

**Map参数**:
```java
List<UserEntity> selectByConditions(Map<String, Object> params);
```

**对象参数**:
```java
int insert(UserEntity user);
```

### 8. 类型处理器 (Type Handlers)

**特性说明**: 处理Java类型与JDBC类型之间的转换，特别适用于枚举类型。

**枚举处理示例**:
```xml
<result column="project_status" property="status" jdbcType="VARCHAR" 
        typeHandler="org.apache.ibatis.type.EnumTypeHandler"/>
```

## 🏛️ 实体类设计

### 设计原则

1. **统一命名规范**: 使用驼峰命名法，与数据库字段对应
2. **审计字段**: 包含创建时间、更新时间、创建人、更新人等
3. **版本控制**: 支持乐观锁的版本字段
4. **关联属性**: 为关联查询预留属性字段

### 示例代码

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Long id;                    // 主键
    private String username;            // 用户名
    private String realName;            // 真实姓名
    private String email;               // 邮箱
    private Integer age;                // 年龄
    private String gender;              // 性别
    private String phone;               // 手机号
    private Long departmentId;          // 部门ID
    
    // 审计字段
    private LocalDateTime createTime;   // 创建时间
    private LocalDateTime updateTime;   // 更新时间
    private String createdBy;           // 创建人
    private String updatedBy;           // 更新人
    private Long version;               // 版本号
    
    // 关联查询结果映射
    private DeptEntity department;      // 关联的部门
    private List<RoleEntity> roles;     // 用户角色列表
    private List<ProjectEntity> projects; // 参与的项目列表
}
```

## 🗺️ Mapper接口设计

### 设计原则

1. **功能分组**: 按照操作类型分组（CRUD、条件查询、关联查询等）
2. **命名规范**: 使用统一的方法命名规范
3. **参数注解**: 使用`@Param`注解明确参数名称
4. **返回类型**: 根据业务需求选择合适的返回类型

### 方法分类

```java
public interface UserMapperXml {
    // 基础CRUD操作
    UserEntity selectById(@Param("id") Long id);
    int insert(UserEntity user);
    int updateSelective(UserEntity user);
    int deleteById(@Param("id") Long id);
    
    // 条件查询 - 动态SQL
    List<UserEntity> selectByConditions(Map<String, Object> params);
    List<UserEntity> selectByUsernameLike(@Param("username") String username);
    List<UserEntity> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    // 关联查询
    UserEntity selectWithDepartment(@Param("id") Long id);
    UserEntity selectWithRoles(@Param("id") Long id);
    
    // 分页查询
    List<UserEntity> selectWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    long countTotal();
    
    // 批量操作
    int batchInsert(@Param("users") List<UserEntity> users);
    int batchDelete(@Param("ids") List<Long> ids);
    
    // 统计查询
    List<Map<String, Object>> countUsersByDepartment();
    List<Map<String, Object>> countUsersByGender();
}
```

## 📋 服务层实现

### 设计原则

1. **事务管理**: 合理使用`@Transactional`注解
2. **异常处理**: 提供清晰的异常信息
3. **日志记录**: 记录关键操作的日志
4. **业务逻辑**: 封装复杂的业务逻辑

### 关键特性

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 默认只读事务
public class UserServiceImpl implements UserService {
    
    private final UserMapperXml userMapper;
    
    @Override
    @Transactional  // 写操作需要读写事务
    public UserEntity save(UserEntity user) {
        // 设置审计字段
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreatedBy("system");
        user.setUpdatedBy("system");
        user.setVersion(1L);
        
        userMapper.insert(user);
        return user;
    }
    
    @Override
    public Page<UserEntity> findWithPagination(Pageable pageable) {
        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();
        
        List<UserEntity> users = userMapper.selectWithPagination(offset, limit);
        long total = userMapper.countTotal();
        
        return new PageImpl<>(users, pageable, total);
    }
}
```

## 🌐 控制器API设计

### RESTful API设计

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // 基础CRUD
    @GetMapping("/{id}")                    // 查询单个用户
    @PostMapping                            // 创建用户
    @PutMapping("/{id}")                    // 更新用户
    @DeleteMapping("/{id}")                 // 删除用户
    
    // 条件查询
    @GetMapping("/search")                  // 多条件查询
    @GetMapping("/search/username")         // 用户名模糊查询
    @GetMapping("/search/age")              // 年龄范围查询
    
    // 关联查询
    @GetMapping("/{id}/with-department")    // 查询用户及部门
    @GetMapping("/{id}/with-roles")         // 查询用户及角色
    
    // 分页查询
    @GetMapping("/page")                    // 分页查询
    @GetMapping("/search/page")             // 条件分页查询
    
    // 统计查询
    @GetMapping("/stats/department")        // 部门统计
    @GetMapping("/stats/gender")            // 性别统计
    
    // 批量操作
    @PostMapping("/batch")                  // 批量创建
    @PutMapping("/batch")                   // 批量更新
    @DeleteMapping("/batch")                // 批量删除
}
```

## 📝 学习要点总结

### 1. MyBatis核心概念

- **SqlSession**: MyBatis的核心接口，用于执行SQL语句
- **Mapper**: 映射器接口，定义数据访问方法
- **ResultMap**: 结果映射，定义查询结果与Java对象的映射关系
- **动态SQL**: 根据条件动态构建SQL语句的机制

### 2. 配置要点

- **数据源配置**: 在application.properties中配置数据库连接
- **MyBatis配置**: 配置mapper扫描路径、XML文件位置等
- **事务管理**: 使用Spring的声明式事务管理

### 3. 性能优化

- **批量操作**: 使用批量插入、更新、删除提高性能
- **分页查询**: 使用数据库层面的分页避免内存溢出
- **延迟加载**: 对于关联查询，可以配置延迟加载
- **缓存机制**: 合理使用一级缓存和二级缓存

### 4. 最佳实践

- **参数校验**: 在Service层进行参数校验
- **异常处理**: 提供清晰的异常信息和处理机制
- **日志记录**: 记录关键操作的执行日志
- **代码复用**: 使用SQL片段和ResultMap继承提高代码复用性

## 🚀 最佳实践建议

### 1. 项目结构组织

```
- 按功能模块组织包结构
- 实体类统一放在entity包
- Mapper接口放在mapper包
- XML文件放在resources/mybatis/mapper目录
- 服务层按业务领域划分
```

### 2. 命名规范

```
- 实体类：XxxEntity
- Mapper接口：XxxMapper
- XML文件：XxxMapper.xml
- 方法命名：selectXxx, insertXxx, updateXxx, deleteXxx
- 参数命名：使用@Param注解明确参数名
```

### 3. SQL编写规范

```
- 使用参数化查询防止SQL注入
- 合理使用索引提高查询性能
- 避免使用SELECT *，明确指定字段
- 复杂查询考虑分解为多个简单查询
```

### 4. 事务管理

```
- 读操作使用@Transactional(readOnly = true)
- 写操作使用@Transactional
- 合理设置事务传播行为和隔离级别
- 避免长事务，及时提交或回滚
```

### 5. 异常处理

```
- 定义业务异常类
- 在Service层处理业务异常
- 在Controller层处理系统异常
- 提供友好的错误信息给前端
```

## 🎓 学习建议

1. **从基础开始**: 先掌握基本的CRUD操作
2. **理解动态SQL**: 这是MyBatis的核心特性
3. **实践关联查询**: 理解一对一、一对多、多对多的映射
4. **掌握分页技术**: 学会处理大数据量的查询
5. **学习性能优化**: 了解缓存、批量操作等优化技术
6. **结合Spring Boot**: 学会在Spring Boot项目中集成MyBatis

通过本模块的学习，你将全面掌握MyBatis框架的使用方法和最佳实践，为实际项目开发打下坚实的基础。
