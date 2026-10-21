# 电商管理系统 (E-commerce System)

## 项目概述

基于 Spring Boot 4 的多角色电商管理系统，支持普通用户、商家、平台管理员三种角色，提供商品管理、订单处理、用户管理等完整功能。

### 技术栈

**后端框架**
- Spring Boot 4.1.1
- Spring Security + JWT 认证
- MyBatis Plus 3.5.17
- Spring Validation

**数据库**
- MySQL 8.0+

**第三方服务**
- 阿里云OSS（对象存储）

**文档工具**
- SpringDoc OpenAPI 3.1.1（Swagger UI）

**开发工具**
- Java 17
- Maven 3.8+
- Lombok

---

## 快速开始

### 1. 环境要求

- JDK 17 或更高版本
- MySQL 8.0+
- Maven 3.8+
- 阿里云 OSS 账号（可选，用于图片上传）

### 2. 数据库配置

创建数据库：
```sql
CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行项目中的数据库初始化脚本（如有提供）。

### 3. 配置文件

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ecommerce
    username: <你的数据库用户名>
    password: <你的数据库密码>

aliyun:
  oss:
    access-key-id: <你的阿里云AccessKey>
    access-key-secret: <你的阿里云Secret>
```

### 4. 启动项目

```bash
# 编译项目
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run
```

或直接在 IDEA 中运行 `ECommerceSystemApplication` 主类。

### 5. 访问接口文档

启动成功后访问：http://localhost:8080/swagger-ui.html

---

## 核心功能模块

### 🔐 认证授权模块
- **用户注册**：支持普通用户和商家注册
- **用户登录**：JWT Token 认证机制
- **角色权限**：基于 Spring Security 的角色控制
  - `ROLE_USER`：普通用户
  - `ROLE_MERCHANT`：商家
  - `ROLE_ADMIN`：管理员

### 👤 用户端功能
- 用户个人信息管理
- 收货地址管理
- 购物车功能
- 商品浏览与搜索
- 订单创建与查询
- 商品评价

### 🏪 商家端功能
- ✅ 商品管理（CRUD）
- ✅ 商品SKU管理
- ✅ 库存管理
- ✅ 商品图片上传（OSS）
- ✅ 商家资料管理
- 订单处理与发货
- 评价查看

### 🛡️ 管理员功能
- ✅ 用户管理（查询、禁用/启用）
- ✅ 商家审核与管理
- ✅ 商品分类管理
- ✅ 商品审核
- ✅ 订单监控
- 平台数据统计

---

## API 接口说明

### 认证相关 `/api/auth`

| 接口 | 方法 | 说明 | 鉴权 |
|------|------|------|------|
| `/register` | POST | 用户/商家注册 | ❌ |
| `/login` | POST | 用户登录 | ❌ |

### 管理员接口 `/api/admin`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 用户管理 | `/users/**` | 用户查询、禁用/启用 |
| 商家管理 | `/merchants/**` | 商家审核、查询 |
| 分类管理 | `/categories/**` | 分类增删改查 |
| 商品管理 | `/products/**` | 商品审核、查询 |
| 订单管理 | `/orders/**` | 订单查询、监控 |

### 商家接口 `/api/merchant`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 商品管理 | `/products/**` | 商品CRUD、上下架 |
| 库存管理 | `/inventories/**` | 库存查询、更新 |
| 资料管理 | `/profile/**` | 商家信息维护 |

### 用户接口 `/api/user`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 个人中心 | `/profile/**` | 个人信息管理 |
| 地址管理 | `/addresses/**` | 收货地址CRUD |
| 购物车 | `/cart/**` | 购物车增删改查 |
| 订单 | `/orders/**` | 订单创建、查询 |
| 评价 | `/reviews/**` | 商品评价 |

### 文件上传 `/api/upload`

| 接口 | 方法 | 说明 | 鉴权 |
|------|------|------|------|
| `/image` | POST | 上传图片到OSS | ✅ |

详细接口参数请查看 Swagger 文档。

---

## 数据库设计

### 核心数据表

| 表名 | 说明 | 状态 |
|------|------|------|
| `users` | 用户表（包含三种角色） | ✅ |
| `categories` | 商品分类表 | ✅ |
| `products` | 商品表 | ✅ |
| `product_skus` | 商品SKU表 | ✅ |
| `inventories` | 库存表 | ✅ |
| `carts` | 购物车表 | ✅ |
| `cart_items` | 购物车明细表 | ✅ |
| `orders` | 订单表 | ✅ |
| `order_items` | 订单明细表 | ✅ |
| `user_addresses` | 用户地址表 | ✅ |

### 用户角色说明

`users` 表中 `role` 字段：
- `user` → 普通用户
- `merchant` → 商家
- `admin` → 平台管理员

---

## 项目结构

```
src/main/java/com/example/es/
├── auth/                    # 认证相关
│   └── CurrentUserProvider.java
├── config/                  # 配置类
│   ├── MybatisPlusConfig.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/              # 控制器层
│   ├── admin/              # 管理员接口
│   ├── auth/               # 认证接口
│   ├── merchant/           # 商家接口
│   ├── user/               # 用户接口（待实现）
│   └── ossController/      # 文件上传
├── entity/                  # 实体类
├── enums/                   # 枚举类
├── exception/               # 异常处理
├── filter/                  # 过滤器（JWT认证）
├── mapper/                  # MyBatis Mapper
├── service/                 # 服务层
└── util/                    # 工具类
```

---

## 开发进度

### ✅ 已完成功能

1. **认证授权**
   - 用户注册、登录
   - JWT Token 生成与验证
   - 基于角色的权限控制

2. **管理员功能**
   - 用户管理（查询、禁用/启用）
   - 商家审核与管理
   - 商品分类管理
   - 商品审核
   - 订单查询

3. **商家功能**
   - 商品完整CRUD
   - SKU管理
   - 库存管理
   - 商家资料维护
   - 商品图片上传

4. **基础设施**
   - 全局异常处理
   - 统一响应格式
   - API 文档（Swagger）
   - OSS 文件上传

### 🚧 待完成功能

1. **用户端接口**（优先级：高）
   - 用户个人信息管理
   - 收货地址管理
   - 购物车操作
   - 商品浏览与搜索
   - 订单创建与查询
   - 商品评价

2. **商家端补充**
   - 订单处理与发货
   - 销售数据统计

3. **功能增强**
   - 商品搜索与筛选
   - 分页优化
   - 缓存机制（Redis）
   - 订单支付集成
   - 物流跟踪

---

## 安全配置

### JWT 配置

```yaml
jwt:
  issuer: Es
  secret: <256位密钥>
  tokenExpireTime: 1440  # 24小时
  tokenHeaderKey: Authorization
  tokenPrefix: Bearer
```

### 接口鉴权规则

在 `SecurityConfig.java` 中配置：

```java
// 公开接口（无需登录）
/api/auth/**
/swagger-ui/**
/v3/api-docs/**

// 需要对应角色权限
/api/admin/**  → ROLE_ADMIN
/api/merchant/** → ROLE_MERCHANT
/api/user/**   → ROLE_USER
```

---

## 常见问题

### 1. 数据库连接失败

检查 `application.yml` 中的数据库配置是否正确，确保 MySQL 服务已启动。

### 2. JWT Token 验证失败

确保请求头中携带正确的 Token：
```
Authorization: Bearer <your_token_here>
```

### 3. 图片上传失败

检查阿里云 OSS 配置信息是否正确，bucket 权限是否开放。

### 4. Swagger 无法访问

确保 SpringDoc 配置已启用：
```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
```

---

## 测试账号（开发环境）

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | - | 需自行注册 |
| 商家 | merchant | - | 需自行注册 |
| 用户 | user | - | 需自行注册 |

注：首次使用请通过 `/api/auth/register` 接口注册测试账号。

---

## 开发规范

### 代码规范
- 使用 Lombok 减少样板代码
- Controller 只负责参数校验和调用 Service
- Service 层处理业务逻辑
- 使用 MyBatis Plus 简化 CRUD 操作

### 命名规范
- Controller：`XxxController`
- Service：`XxxService` / `XxxServiceImpl`
- Mapper：`XxxMapper`
- Entity：对应表名的驼峰命名

### 响应格式
统一使用 `ResponseResult<T>` 封装：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

---

## 许可证

[待补充]

---

## 联系方式

项目维护者：[待补充]

---

## 更新日志

### [0.0.1-SNAPSHOT] - 2024-10-21
- 项目初始化
- 完成管理员、商家核心功能
- 集成 JWT 认证
- 集成阿里云 OSS
