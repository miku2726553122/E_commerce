# 电商管理系统 (E-commerce System)

## 项目概述

基于 Spring Boot 4 的多角色电商管理系统，支持普通用户、商家、平台管理员三种角色，提供商品管理、订单处理、用户管理等完整功能。

### 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 4.1.1 |
| **安全认证** | Spring Security + JWT | 7.1.1 |
| **ORM 框架** | MyBatis Plus | 3.5.17 |
| **参数校验** | Spring Validation | - |
| **数据库** | MySQL | 8.0+ |
| **对象存储** | 阿里云 OSS | v2-0.5.1 |
| **API 文档** | SpringDoc OpenAPI | 3.1.1 |
| **密码加密** | BCrypt (jbcrypt) | 0.4 |
| **开发工具** | Lombok | 1.18.46 |

**运行环境**
- JDK 21
- Maven 3.8+
- MySQL 8.0+

---

## 快速开始

### 1. 克隆项目

```bash
git clone git@github.com:miku2726553122/E_commerce.git
cd E_commerceSystem
```

### 2. 数据库配置

创建数据库：
```sql
CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行项目中的 SQL 脚本：
- `src/main/resources/sql/ecommerce.sql` - 建表语句
- `src/main/resources/sql/test-data.sql` - 测试数据（可选）

### 3. 配置文件

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ecommerce
    username: <你的数据库用户名>
    password: <你的数据库密码>

# 阿里云 OSS 配置（可选）
aliyun:
  oss:
    access-key-id: ${OSS_ACCESS_KEY_ID}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET}
    bucket-name: <你的Bucket名称>

# JWT 密钥（生产环境请使用环境变量）
jwt:
  secret: ${JWT_SECRET:你的256位密钥}
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

### 🔐 认证授权

| 功能 | 说明 |
|------|------|
| 用户注册 | 区分用户注册和商家注册，分别设置默认角色 |
| 用户登录 | JWT Token 认证，24小时有效期 |
| 角色权限 | 基于 Spring Security 的角色访问控制 |
| 401/403 处理 | 统一 JSON 格式错误响应 |

**角色说明**
- `USER` - 普通用户
- `MERCHANT` - 商家
- `ADMIN` - 管理员

### 👤 用户端功能

| 模块 | 功能 |
|------|------|
| 个人中心 | 查看/修改个人信息、修改密码 |
| 收货地址 | 地址增删改查 |
| 购物车 | 购物车增删改查、数量修改 |
| 商品浏览 | 分类浏览、商品搜索、商品详情（无需登录） |
| 订单管理 | 创建订单、取消订单、确认收货、订单查询 |

### 🏪 商家端功能

| 模块 | 功能 |
|------|------|
| 商品管理 | 商品 CRUD、上下架、审核状态管理 |
| SKU 管理 | 规格、价格、图片管理 |
| 库存管理 | 库存查询、库存更新（乐观锁） |
| 商家资料 | 店铺信息维护、Logo 上传 |
| 订单处理 | 商家订单查看、确认发货 |

### 🛡️ 管理员功能

| 模块 | 功能 |
|------|------|
| 用户管理 | 用户查询、启用/禁用账号 |
| 商家管理 | 商家审核、商家查询、添加商家 |
| 分类管理 | 分类增删改查、分类下商品检查 |
| 商品审核 | 商品审核通过/拒绝 |
| 订单监控 | 全平台订单查询 |

---

## API 接口说明

### 认证接口 `/auth`

| 接口 | 方法 | 说明 | 鉴权 |
|------|------|------|------|
| `/auth/login` | POST | 用户登录 | ❌ |
| `/auth/register/user` | POST | 用户注册（角色 USER） | ❌ |
| `/auth/register/merchant` | POST | 商家注册（角色 MERCHANT） | ❌ |

### 管理员接口 `/admin`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 用户管理 | `/admin/users/**` | 用户查询、禁用/启用 |
| 商家管理 | `/admin/merchants/**` | 商家审核、查询、添加 |
| 分类管理 | `/admin/categories/**` | 分类增删改查 |
| 商品管理 | `/admin/products/**` | 商品审核、查询 |
| 订单管理 | `/admin/orders/**` | 订单查询、监控 |

### 商家接口 `/merchant`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 商品管理 | `/merchant/products/**` | 商品 CRUD、上下架 |
| 库存管理 | `/merchant/inventories/**` | 库存查询、更新 |
| 资料管理 | `/merchant/profile/**` | 商家信息维护 |
| 订单管理 | `/merchant/orders/**` | 订单查看、发货 |

### 用户接口 `/user`

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 个人中心 | `/user/profile/**` | 个人信息管理 |
| 地址管理 | `/user/addresses/**` | 收货地址 CRUD |
| 购物车 | `/user/cart/**` | 购物车增删改查 |
| 商品浏览 | `/user/products/**` | 商品搜索、详情（公开） |
| 分类浏览 | `/user/categories` | 分类列表（公开） |
| 订单管理 | `/user/orders/**` | 订单创建、查询、取消、收货 |

### 文件上传 `/api`

| 接口 | 方法 | 说明 | 鉴权 |
|------|------|------|------|
| `/api/upload/image` | POST | 上传图片到 OSS | ✅ |

详细接口参数请查看 [Swagger 文档](http://localhost:8080/swagger-ui.html)。

---

## 数据库设计

### 核心数据表

| 表名 | 说明 | 乐观锁 |
|------|------|--------|
| `users` | 用户表（包含三种角色） | - |
| `categories` | 商品分类表 | - |
| `products` | 商品表 | ✅ |
| `product_skus` | 商品 SKU 表 | - |
| `inventories` | 库存表 | ✅ |
| `carts` | 购物车表 | - |
| `cart_items` | 购物车明细表 | - |
| `orders` | 订单表 | ✅ |
| `order_items` | 订单明细表 | - |
| `user_addresses` | 用户地址表 | - | 

### 用户角色说明

`users` 表中 `role` 字段：
- `USER` → 普通用户
- `MERCHANT` → 商家
- `ADMIN` → 平台管理员

---

## 项目结构

```
src/main/java/com/example/es/
├── auth/                       # 认证主体
│   └── UserPrincipal.java
├── config/                     # 配置类
│   ├── MybatisPlusConfig.java
│   ├── OpenApiConfig.java
│   ├── SecurityConfig.java
│   └── ThreadPoolConfig.java
├── controller/                 # 控制器层
│   ├── admin/                  # 管理员接口
│   │   ├── CategoryManagementController.java
│   │   ├── ProductReviewandOrderManagementController.java
│   │   └── UsersAndMerchantsController.java
│   ├── auth/                   # 认证接口
│   │   └── AuthController.java
│   ├── merchant/               # 商家接口
│   │   ├── MerchantInventoryController.java
│   │   ├── MerchantOrderController.java
│   │   ├── MerchantProductController.java
│   │   └── MerchantProfileController.java
│   ├── ossController/          # 文件上传
│   │   └── UploadController.java
│   └── user/                   # 用户接口
│       ├── ProductBrowsingController.java
│       ├── ShoppingCartandDeliveryAddressController.java
│       ├── UserInfoController.java
│       └── UserOrderController.java
├── entity/                     # 实体类
├── enums/                      # 枚举类
│   └── ResponseCodeEnum.java
├── exception/                  # 异常处理
│   ├── BaseExceptionInterface.java
│   ├── BizException.java
│   └── GlobalExceptionHandler.java
├── filter/                     # 过滤器
│   └── JwtAuthFilter.java
├── mapper/                     # MyBatis Mapper
├── service/                    # 服务层
│   ├── impl/                   # 服务实现
│   └── *.java                  # 服务接口
├── utils/                      # 工具类
│   ├── AliOssUtils.java
│   ├── JwtUtils.java
│   ├── PasswordUtil.java
│   ├── Response.java
│   └── SecurityUtils.java
└── vo/                         # 视图对象
    ├── admin/
    ├── auth/
    ├── merchant/
    └── user/
```

---

## 安全配置

### JWT 配置

```yaml
jwt:
  issuer: Es
  secret: ${JWT_SECRET:你的256位密钥}  # 生产环境必须使用环境变量
  tokenExpireTime: 1440  # 24小时
  tokenHeaderKey: Authorization
  tokenPrefix: Bearer
```

### 接口鉴权规则

在 `SecurityConfig.java` 中配置：

```
公开接口（无需登录）
├── /auth/**
├── /user/categories
├── /user/products/**
├── /swagger-ui/**
└── /v3/api-docs/**

需要登录认证
└── /user/** (除上述公开接口外)

需要角色权限
├── /admin/**    → ROLE_ADMIN
└── /merchant/** → ROLE_MERCHANT
```

### 请求头格式

```
Authorization: Bearer <your_token_here>
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

### 5. 401 未登录或 Token 已过期

检查 Token 是否有效，是否已过期（默认24小时）。

### 6. 403 权限不足

确认当前用户角色是否有权限访问该接口：
- `/admin/**` 需要 `ADMIN` 角色
- `/merchant/**` 需要 `MERCHANT` 角色

---

## 测试账号（开发环境）

| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 管理员 | admin | - | 需自行注册或数据库插入 |
| 商家 | merchant | - | 通过 `/auth/register/merchant` 注册 |
| 用户 | user | - | 通过 `/auth/register/user` 注册 |

---

## 开发规范

### 代码规范

- 使用 Lombok 减少样板代码（`@Data`、`@Builder`、`@Slf4j`）
- Controller 只负责参数校验和调用 Service
- Service 层处理业务逻辑
- 使用 MyBatis Plus 简化 CRUD 操作
- 关键业务方法添加日志记录

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| Controller | `XxxController` | `UserOrderController` |
| Service 接口 | `XxxService` | `OrdersService` |
| Service 实现 | `XxxServiceImpl` | `OrdersServiceImpl` |
| Mapper | `XxxMapper` | `OrdersMapper` |
| Entity | 表名驼峰命名 | `Orders` |
| VO | `XxxReqVO` / `XxxRspVO` | `CreateOrderReqVO` |

### 响应格式

统一使用 `Response` 封装：
```json
{
  "success": true,
  "message": "操作成功",
  "data": { ... }
}
```

### 日志规范

使用 `@Slf4j` 注解，关键操作记录日志：
```
[模块名] 操作描述, 关键参数=值
```

示例：
```
[创建订单] 开始, userId=1, addressId=5, 商品数量=2
[创建订单] 库存锁定完成
[创建订单] 完成, orderNo=ORDa1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6
```

---

## 更新日志

### [0.0.2-SNAPSHOT] - 2026-10-22

**安全增强**
- 添加角色访问控制：`/admin/**` 需要 ADMIN 角色，`/merchant/**` 需要 MERCHANT 角色
- 统一 401/403 JSON 错误响应
- JWT 密钥支持环境变量配置

**功能优化**
- 注册接口拆分：用户注册 `/auth/register/user` 和商家注册 `/auth/register/merchant`
- 订单号生成改用 UUID，保证全局唯一
- 库存锁定改为串行乐观锁重试，防止超卖
- 分类删除前检查关联商品

**问题修复**
- 修复 `confirmShipment` 订单状态值错误
- 修复地址 VO 缺少省市区字段
- 修复多处空指针风险
- 添加乐观锁重试机制到商品状态更新

**开发体验**
- 关键业务方法添加日志记录
- 控制台输出更清晰的请求流程

### [0.0.1-SNAPSHOT] - 2026-10-21

- 项目初始化
- 完成管理员、商家核心功能
- 集成 JWT 认证
- 集成阿里云 OSS

---

## 许可证

[待补充]

---

## 联系方式

项目维护者：[待补充]
