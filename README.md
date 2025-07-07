# 乡村支教管理系统

## 项目简介

乡村支教管理系统是一个基于Spring Boot 3.x + MyBatis的Web应用，用于管理乡村支教活动中的用户、学生、角色等信息。

## 技术栈

- **后端框架**: Spring Boot 3.4.5
- **数据库**: MySQL 8.0
- **ORM框架**: MyBatis 3.0.3
- **安全框架**: Spring Security
- **API文档**: SpringDoc OpenAPI (现代Swagger替代方案)
- **构建工具**: Maven

## 为什么选择 SpringDoc OpenAPI？

### ✅ SpringDoc OpenAPI 优势
- **现代化**: 专门为Spring Boot 3.x设计
- **轻量级**: 依赖更少，启动更快
- **活跃维护**: 持续更新，社区支持好
- **配置简单**: 开箱即用，配置简洁
- **性能优秀**: 比传统Swagger性能更好

### ❌ 传统Swagger 2.x 问题
- **过时**: 主要支持Spring Boot 2.x
- **维护少**: 更新频率降低
- **依赖重**: 包含不必要的依赖
- **配置复杂**: 需要更多配置

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 启动项目

```bash
# 克隆项目
git clone <repository-url>
cd Rural_tutoring

# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run
```

### 3. 访问地址

- **应用地址**: http://localhost:8080
- **API文档**: http://localhost:8080/swagger-ui.html
- **OpenAPI规范**: http://localhost:8080/v3/api-docs

## API文档使用

### SpringDoc OpenAPI UI

项目集成了SpringDoc OpenAPI，提供了现代化的API文档界面：

1. 启动项目后，访问 http://localhost:8080/swagger-ui.html
2. 在OpenAPI UI中可以：
   - 查看所有API接口
   - 在线测试API接口
   - 查看请求/响应示例
   - 下载OpenAPI规范文档

### API分组

API按功能模块分组：

- **用户管理**: 用户的增删改查操作
- **学生管理**: 学生的增删改查操作  
- **角色管理**: 角色的增删改查操作，支持分页查询

### 示例请求

#### 分页查询角色
```bash
GET /api/role?page=1&size=10&name=管理员
```

#### 新增用户
```bash
POST /api/user
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com"
}
```

## 项目结构

```
src/main/java/com/example/rural_tutoring/
├── config/          # 配置类
│   ├── OpenApiConfig.java    # SpringDoc OpenAPI配置
│   ├── SecurityConfig.java   # 安全配置
│   └── Result.java          # 统一响应结果
├── controller/      # 控制器层
│   ├── RoleController.java
│   ├── StudentController.java
│   └── UserController.java
├── entity/         # 实体类
│   ├── Role.java
│   ├── Student.java
│   └── User.java
├── mapper/         # MyBatis映射接口
├── model/          # 模型类
│   ├── DTO/        # 数据传输对象
│   └── PageResult.java  # 分页结果
└── service/        # 服务层
    └── impl/       # 服务实现
```

## 开发说明

### 添加新的API

1. 在对应的Controller中添加方法
2. 使用OpenAPI注解描述API：
   ```java
   @Operation(summary = "接口标题", description = "接口描述")
   @Parameter(description = "参数描述", example = "示例值")
   ```

3. 为实体类添加Schema注解：
   ```java
   @Schema(description = "实体描述")
   public class Entity {
       @Schema(description = "字段描述", example = "示例值")
       private String field;
   }
   ```

### 分页查询

项目提供了通用的分页查询支持：

```java
@GetMapping
public Result<PageResult<Entity>> getEntities(
    @RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer size,
    @RequestParam(required = false) String name) {
    // 实现分页逻辑
}
```

## 许可证

MIT License

## 作者

jiashuhang 