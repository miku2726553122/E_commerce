-- 为 cart_items、orders、products 表添加 version 字段（乐观锁）
-- inventories 表已有 version 字段，无需重复添加

-- 1. 购物车明细表
ALTER TABLE cart_items 
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）';

-- 2. 订单表
ALTER TABLE orders 
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）';

-- 3. 商品表
ALTER TABLE products 
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）';

-- 验证字段是否添加成功
-- SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_DEFAULT, COLUMN_COMMENT 
-- FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_NAME IN ('cart_items', 'orders', 'products') 
-- AND COLUMN_NAME = 'version'
-- ORDER BY TABLE_NAME;
