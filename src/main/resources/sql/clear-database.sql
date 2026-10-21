-- 清空电商系统所有表数据
-- 执行前请确保已备份重要数据！

USE ecommerce;

SET FOREIGN_KEY_CHECKS = 0;

-- 按外键依赖顺序清空表（从子表到父表）

-- 1. 订单明细表（依赖 orders, products, product_skus）
TRUNCATE TABLE order_items;

-- 2. 订单表（依赖 users）
TRUNCATE TABLE orders;

-- 3. 购物车明细表（依赖 carts, products, product_skus）
TRUNCATE TABLE cart_items;

-- 4. 购物车表（依赖 users）
TRUNCATE TABLE carts;

-- 5. 库存表（依赖 product_skus）
TRUNCATE TABLE inventories;

-- 6. SKU表（依赖 products）
TRUNCATE TABLE product_skus;

-- 7. 商品表（依赖 categories, users）
TRUNCATE TABLE products;

-- 8. 分类表（自引用，无外部依赖）
TRUNCATE TABLE categories;

-- 9. 用户地址表（依赖 users）
TRUNCATE TABLE user_addresses;

-- 10. 用户表
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- 验证清空结果
SELECT 'order_items' AS table_name, COUNT(*) AS row_count FROM order_items
UNION ALL
SELECT 'orders', COUNT(*) FROM orders
UNION ALL
SELECT 'cart_items', COUNT(*) FROM cart_items
UNION ALL
SELECT 'carts', COUNT(*) FROM carts
UNION ALL
SELECT 'inventories', COUNT(*) FROM inventories
UNION ALL
SELECT 'product_skus', COUNT(*) FROM product_skus
UNION ALL
SELECT 'products', COUNT(*) FROM products
UNION ALL
SELECT 'categories', COUNT(*) FROM categories
UNION ALL
SELECT 'user_addresses', COUNT(*) FROM user_addresses
UNION ALL
SELECT 'users', COUNT(*) FROM users;
