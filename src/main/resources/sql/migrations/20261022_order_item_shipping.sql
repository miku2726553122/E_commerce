-- Run once against an existing database before starting the updated backend.
-- Only adds shipping timestamps; no orders or inventory quantities are removed.
ALTER TABLE order_items ADD COLUMN shipped_at DATETIME NULL COMMENT '商品发货时间';

-- Preserve the fulfillment state of orders shipped before this migration.
UPDATE order_items oi
JOIN orders o ON o.id = oi.order_id
SET oi.shipped_at = COALESCE(o.shipped_at, o.completed_at, o.updated_at, o.created_at)
WHERE o.order_status IN ('已发货', '已完成') AND oi.shipped_at IS NULL;
