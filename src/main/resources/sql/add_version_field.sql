-- 为 inventories 表添加 version 字段（乐观锁）
ALTER TABLE inventories 
ADD COLUMN version INT NOT NULL DEFAULT 0 COMMENT '版本号（乐观锁）';

-- 验证字段是否添加成功
-- SELECT COLUMN_NAME, DATA_TYPE, COLUMN_DEFAULT, COLUMN_COMMENT 
-- FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_NAME = 'inventories' AND COLUMN_NAME = 'version';
