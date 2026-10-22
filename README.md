# 电商管理系统

<div align="center">

基于 Spring Boot 4 的多角色电商平台，支持用户、商家、管理员三种角色。

[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)]()
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.17-blue)]()
[![License](https://img.shields.io/badge/License-MIT-yellow)]()

</div>

---

## 功能特性

### 认证授权
- JWT Token 无状态认证（24小时有效期）
- 基于角色的访问控制（USER / MERCHANT / ADMIN）
- 用户注册与商家注册分离
- 统一 401/403 JSON 错误响应

### 用户端
| 功能 | 说明 |
|------|------|
| 个人中心 | 信息管理、密码修改 |
| 收货地址 | 地址增删改查 |
| 购物车 | 商品添加、数量修改、删除 |
| 商品浏览 | 分类浏览、搜索、详情（无需登录） |
| 订单管理 | 创建、取消、确认收货、查询 |

### 商家端
| 功能 | 说明 |
|------|------|
| 商品管理 | CRUD、上下架、审核状态 |
| SKU 管理 | 规格、价格、图片 |
| 库存管理 | 查询、更新（乐观锁防超卖） |
| 店铺管理 | 信息维护、Logo 上传 |
| 订单处理 | 查看、确认发货 |

### 管理员端
| 功能 | 说明 |
|------|------|
| 用户管理 | 查询、启用/禁用 |
| 商家管理 | 审核、查询、添加 |
| 分类管理 | 增删改查、关联商品检查 |
| 商品审核 | 通过/拒绝 |
| 订单监控 | 全平台订单查询 |

---

## 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 4.1.1 |
| 安全认证 | Spring Security + JJWT | 7.1.1 / 0.11.5 |
| ORM | MyBatis Plus | 3.5.17 |
| 数据库 | MySQL | 8.0+ |
| 对象存储 | 阿里云 OSS | v2-0.5.1 |
| API 文档 | SpringDoc OpenAPI | 3.1.1 |
| 密码加密 | BCrypt (jbcrypt) | 0.4 |

**运行环境：** JDK 21 / Maven 3.8+

---

## 快速开始

### 1. 克隆项目

```bash
git clone git@github.com:miku2726553122/E_commerce.git
cd E_commerceSystem
```

### 2. 初始化数据库

```sql
CREATE DATABASE ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行 `src/main/resources/sql/ecommerce.sql` 建表。

### 3. 修改配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ecommerce
    username: root
    password: your_password

jwt:
  secret: ${JWT_SECRET:your_256bit_secret}

aliyun:
  oss:
    access-key-id: ${OSS_ACCESS_KEY_ID}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET}
    bucket-name: your_bucket
```

### 4. 启动运行

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

或 IDEA 运行 `ECommerceSystemApplication`。

### 5. 访问文档

启动后访问：http://localhost:8080/swagger-ui.html

---

## API 接口

### 认证接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|:----:|
| POST | `/auth/login` | 用户登录 | ❌ |
| POST | `/auth/register/user` | 用户注册 | ❌ |
| POST | `/auth/register/merchant` | 商家注册 | ❌ |

### 用户接口（需登录）

| 模块 | 路径 |
|------|------|
| 个人中心 | `/user/profile/**` |
| 地址管理 | `/user/addresses/**` |
| 购物车 | `/user/cart/**` |
| 订单管理 | `/user/orders/**` |
| 商品浏览 | `/user/products/**`（公开） |
| 分类列表 | `/user/categories`（公开） |

### 商家接口（需 MERCHANT 角色）

| 模块 | 路径 |
|------|------|
| 商品管理 | `/merchant/products/**` |
| 库存管理 | `/merchant/inventories/**` |
| 店铺管理 | `/merchant/profile/**` |
| 订单处理 | `/merchant/orders/**` |

### 管理员接口（需 ADMIN 角色）

| 模块 | 路径 |
|------|------|
| 用户管理 | `/admin/users/**` |
| 商家管理 | `/admin/merchants/**` |
| 分类管理 | `/admin/categories/**` |
| 商品审核 | `/admin/products/**` |
| 订单监控 | `/admin/orders/**` |

### 文件上传

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/upload/image` | 上传图片到 OSS |

---

## 数据库设计

| 表名 | 说明 | 乐观锁 |
|------|------|:------:|
| `users` | 用户表 | - |
| `categories` | 商品分类表 | - |
| `products` | 商品表 | ✅ |
| `product_skus` | 商品 SKU 表 | - |
| `inventories` | 库存表 | ✅ |
| `carts` | 购物车表 | - |
| `cart_items` | 购物车明细表 | - |
| `orders` | 订单表 | ✅ |
| `order_items` | 订单明细表 | - |
| `user_addresses` | 用户地址表 | - |

---

## 项目结构

```
src/main/java/com/example/es/
├── auth/                 # 认证主体
├── config/               # 配置类（Security、MyBatis、线程池）
├── controller/           # 控制器
│   ├── admin/            # 管理员接口
│   ├── auth/             # 认证接口
│   ├── merchant/         # 商家接口
│   ├── user/             # 用户接口
│   └── ossController/    # 文件上传
├── entity/               # 实体类
├── enums/                # 枚举类
├── exception/            # 异常处理
├── filter/               # JWT 过滤器
├── mapper/               # MyBatis Mapper
├── service/              # 服务层
│   └── impl/             # 服务实现
├── utils/                # 工具类
└── vo/                   # 视图对象
    ├── admin/
    ├── auth/
    ├── merchant/
    └── user/
```

---

## 安全配置

### 请求认证

```
Authorization: Bearer <token>
```

### 访问控制规则

```
公开访问
├── /auth/**
├── /user/categories
└── /user/products/**

需要登录
└── /user/**（除公开接口外）

需要角色权限
├── /admin/**    → ROLE_ADMIN
└── /merchant/** → ROLE_MERCHANT
```

---

## 常见问题

<details>
<summary><b>数据库连接失败</b></summary>

检查 `application.yml` 中的数据库配置，确保 MySQL 服务已启动。
</details>

<details>
<summary><b>JWT Token 验证失败</b></summary>

确保请求头格式正确：`Authorization: Bearer <token>`
</details>

<details>
<summary><b>401 未登录</b></summary>

Token 可能已过期（默认24小时），请重新登录获取新 Token。
</details>

<details>
<summary><b>403 权限不足</b></summary>

确认用户角色：
- `/admin/**` 需要 `ADMIN` 角色
- `/merchant/**` 需要 `MERCHANT` 角色
</details>

<details>
<summary><b>图片上传失败</b></summary>

检查阿里云 OSS 配置和 Bucket 权限。
</details>

---

## 开发规范

### 代码风格

- 使用 Lombok 注解（`@Data`、`@Builder`、`@Slf4j`）
- Controller 负责参数校验，Service 处理业务逻辑
- 关键业务方法添加日志记录

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| Controller | `XxxController` | `UserOrderController` |
| Service | `XxxService` / `XxxServiceImpl` | `OrdersService` |
| Mapper | `XxxMapper` | `OrdersMapper` |
| Entity | 表名驼峰 | `Orders` |
| VO | `XxxReqVO` / `XxxRspVO` | `CreateOrderReqVO` |

### 响应格式

```json
{
  "success": true,
  "message": "操作成功",
  "data": { }
}
```

### 日志格式

```
[模块名] 操作描述, 参数=值
```

示例：
```
[创建订单] 开始, userId=1, 商品数量=2
[创建订单] 库存锁定完成
[创建订单] 完成, orderNo=ORDa1b2c3d4...
```

---

## 更新日志

### v0.0.2 (2026-10-22)

**安全增强**
- 角色访问控制：`/admin/**` → ADMIN，`/merchant/**` → MERCHANT
- 统一 401/403 JSON 响应
- JWT 密钥支持环境变量

**功能优化**
- 注册接口拆分（用户/商家）
- 订单号改用 UUID
- 库存锁定改用乐观锁防超卖
- 分类删除前检查关联商品

**问题修复**
- 订单状态值统一
- 地址 VO 补充省市区字段
- 多处空指针风险修复
- 商品状态更新添加乐观锁重试

### v0.0.1 (2026-10-21)

- 项目初始化
- 完成管理员、商家核心功能
- 集成 JWT 认证、阿里云 OSS

---

## 许可证

[MIT License]()

---

<div align="center">

**作者：** miku2726553122  
**仓库：** [GitHub](https://github.com/miku2726553122/E_commerce)

</div>
