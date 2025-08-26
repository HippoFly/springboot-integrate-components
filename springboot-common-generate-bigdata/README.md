# Spring Boot 企业大数据生成器

## 项目简介

`springboot-common-generate-bigdata` 是一个统一的企业级大数据生成模块，专为多个Spring Boot项目提供一致、真实的测试数据。该模块生成包含用户、部门、项目、角色等复杂关系的企业数据结构。

## 核心特性

- 🚀 **一键生成**：简单输入用户数量，自动生成完整的企业数据结构
- 🏢 **固定企业架构**：8个预定义部门，6个标准角色
- 👥 **真实数据**：中文姓名、真实邮箱格式、合理的年龄分布
- 🔗 **复杂关系**：用户-部门、用户-项目、用户-角色的多重关系
- 🎨 **可视化界面**：基于Thymeleaf的现代化Web界面
- 📡 **REST API**：完整的API接口支持程序化调用
- 📊 **数据统计**：实时预览和详细的数据分布统计

## 快速开始

### 启动应用

```bash
cd springboot-common-generate-bigdata
mvn spring-boot:run
```

### 访问界面

- **Web界面**: http://localhost:8080
- **API文档**: http://localhost:8080/api/health

### 使用方法

1. 在Web界面输入用户数量（1-100000）
2. 点击"一键生成数据"
3. 查看生成结果和数据统计

## API接口

### 生成数据

```bash
# 生成数据（返回统计信息）
POST /api/generate?userCount=1000

# 生成完整数据（返回所有实体）
POST /api/generate/full?userCount=1000

# 仅生成用户数据
POST /api/generate/users?userCount=1000

# 健康检查
GET /api/health
```

## 数据结构

### 固定数据

**部门（8个）**：
- 技术研发部、产品设计部、市场运营部、人力资源部
- 财务管理部、质量保证部、客户服务部、行政管理部

**角色（6个）**：
- 系统管理员、部门经理、项目经理
- 高级工程师、工程师、专员

### 动态数据

- **用户数量**：根据输入参数生成
- **项目数量**：用户数量的1/4
- **关系分配**：
  - 每个用户分配到一个部门
  - 每个用户随机分配1-3个角色
  - 每个用户参与0-5个项目

## 实体模型

### User（用户）
```java
- Long id
- String username
- String email
- Integer age
- String realName
- String gender
- String phone
- Department department
- List<Project> projects
- List<Role> roles
```

### Department（部门）
```java
- Long id
- String name
- String description
- List<User> users
```

### Project（项目）
```java
- Long id
- String name
- String description
- LocalDate startDate
- LocalDate endDate
- ProjectStatus status
- List<User> users
```

### Role（角色）
```java
- Long id
- String roleName
- String description
- RoleStatus status
```

## 技术栈

- **Spring Boot 2.7.18**
- **Thymeleaf** - 模板引擎
- **Bootstrap 5** - UI框架
- **Lombok** - 代码简化
- **Jackson** - JSON处理
- **Maven** - 构建工具

## 项目结构

```
src/main/java/com/common/bigdata/
├── BigDataGeneratorApplication.java     # 启动类
├── controller/
│   ├── DataGeneratorController.java     # Web控制器
│   └── ApiController.java               # REST API控制器
├── entity/
│   ├── base/BaseAuditEntity.java        # 审计基类
│   └── core/                            # 核心实体
│       ├── User.java
│       ├── Department.java
│       ├── Project.java
│       └── Role.java
├── generator/
│   ├── BigDataGenerator.java            # 数据生成器
│   └── GenerationResult.java            # 生成结果
└── constant/
    └── PresetData.java                   # 预设数据常量

src/main/resources/
├── application.yml                       # 配置文件
└── templates/                           # Thymeleaf模板
    ├── index.html                       # 主页
    └── result.html                      # 结果页
```

## 配置说明

### application.yml
```yaml
server:
  port: 8080

spring:
  thymeleaf:
    cache: false
    
logging:
  level:
    com.common.bigdata: INFO
```

## 使用示例

### Java API调用
```java
@Autowired
private BigDataGenerator generator;

// 生成1000个用户的数据
GenerationResult result = generator.generateData(1000);

// 获取生成的用户
List<User> users = result.getUsers();
List<Department> departments = result.getDepartments();
List<Project> projects = result.getProjects();
List<Role> roles = result.getRoles();
```

### REST API调用
```bash
curl -X POST "http://localhost:8080/api/generate?userCount=1000"
```

## 数据特点

1. **真实性**：使用真实的中文姓名、合理的年龄分布（22-60岁）
2. **一致性**：所有模块使用相同的数据结构和生成规则
3. **完整性**：包含审计字段（创建时间、更新时间、版本等）
4. **关系性**：复杂的多对多、一对多关系模拟真实企业结构

## 扩展说明

该模块设计为其他Spring Boot模块的数据源，其他模块应适配此模块的数据格式，而非反向适配。这确保了数据的一致性和模块的独立性。

## 许可证

MIT License
