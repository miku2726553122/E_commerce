-- 电商系统测试数据
-- 使用前提：已执行建库 SQL，并且 users 表中存在：
--   admin、user001、merchant001
-- 本脚本不会插入、修改或删除 users 表数据。

USE ecommerce;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

-- 通过用户名获取用户 ID，避免依赖 users 表的自增值。
SET @buyer_id = (SELECT id FROM users WHERE username = 'user001' LIMIT 1);
SET @merchant_id = (SELECT id FROM users WHERE username = 'merchant001' LIMIT 1);

-- 1. 商品分类
-- 使用 101+ 的测试 ID，避免与建库脚本中 1~5 的初始化分类冲突。
INSERT INTO categories
    (id, parent_id, category_name, category_image, sort_order, status)
VALUES
    (101, 0, '数码配件', NULL, 1, 1),
    (102, 0, '电脑办公', NULL, 2, 1),
    (103, 0, '男装', NULL, 3, 1),
    (104, 0, '厨房电器', NULL, 4, 1),
    (105, 0, '食品生鲜', NULL, 5, 1),
    (106, 0, '美妆个护', NULL, 6, 1)
ON DUPLICATE KEY UPDATE
    parent_id = VALUES(parent_id),
    category_name = VALUES(category_name),
    category_image = VALUES(category_image),
    sort_order = VALUES(sort_order),
    status = VALUES(status);

-- 2. 商品
INSERT INTO products
    (id, merchant_id, category_id, product_name, product_code,
     main_image, description, detail_content, base_price,
     sale_status, audit_status)
VALUES
    (1001, @merchant_id, 101, '无线蓝牙耳机', 'TEST-P-1001',
     '/images/test/bluetooth-headset.jpg',
     '轻量降噪蓝牙耳机，适合通勤和运动。',
     '支持蓝牙 5.3，续航约 30 小时，Type-C 充电。',
     199.00, 1, 1),
    (1002, @merchant_id, 102, '机械键盘', 'TEST-P-1002',
     '/images/test/mechanical-keyboard.jpg',
     '紧凑型机械键盘，适合办公和游戏。',
     '87 键布局，热插拔轴体，支持有线连接。',
     299.00, 1, 1),
    (1003, @merchant_id, 103, '纯棉短袖 T 恤', 'TEST-P-1003',
     '/images/test/cotton-tshirt.jpg',
     '透气纯棉短袖，适合日常穿着。',
     '面料柔软亲肤，多尺码可选。',
     89.90, 1, 1),
    (1004, @merchant_id, 104, '便携榨汁杯', 'TEST-P-1004',
     '/images/test/portable-juicer.jpg',
     '便携式榨汁杯，USB 充电。',
     '食品级杯体，支持果汁、奶昔等饮品制作。',
     159.00, 1, 1),
    (1005, @merchant_id, 105, '低糖燕麦片', 'TEST-P-1005',
     '/images/test/oatmeal.jpg',
     '低糖高纤燕麦片，早餐冲泡方便。',
     '独立小袋包装，常温保存。',
     39.90, 0, 1)
ON DUPLICATE KEY UPDATE
    merchant_id = VALUES(merchant_id),
    category_id = VALUES(category_id),
    product_name = VALUES(product_name),
    main_image = VALUES(main_image),
    description = VALUES(description),
    detail_content = VALUES(detail_content),
    base_price = VALUES(base_price),
    sale_status = VALUES(sale_status),
    audit_status = VALUES(audit_status);

-- 3. 商品 SKU
INSERT INTO product_skus
    (id, product_id, sku_code, sku_name, specification,
     original_price, sale_price, image_url, status)
VALUES
    (2001, 1001, 'TEST-SKU-2001', '黑色', '{"颜色":"黑色"}',
     229.00, 179.00, '/images/test/bluetooth-headset-black.jpg', 1),
    (2002, 1001, 'TEST-SKU-2002', '白色', '{"颜色":"白色"}',
     239.00, 189.00, '/images/test/bluetooth-headset-white.jpg', 1),
    (2003, 1002, 'TEST-SKU-2003', '茶轴', '{"轴体":"茶轴","颜色":"黑色"}',
     329.00, 279.00, '/images/test/mechanical-keyboard-brown.jpg', 1),
    (2004, 1003, 'TEST-SKU-2004', '黑色 L 码', '{"颜色":"黑色","尺码":"L"}',
     99.90, 79.90, '/images/test/cotton-tshirt-black.jpg', 1),
    (2005, 1003, 'TEST-SKU-2005', '白色 M 码', '{"颜色":"白色","尺码":"M"}',
     99.90, 79.90, '/images/test/cotton-tshirt-white.jpg', 1),
    (2006, 1004, 'TEST-SKU-2006', '樱花粉', '{"颜色":"樱花粉"}',
     179.00, 149.00, '/images/test/portable-juicer-pink.jpg', 1),
    (2007, 1005, 'TEST-SKU-2007', '原味 500g', '{"口味":"原味","规格":"500g"}',
     49.90, 39.90, '/images/test/oatmeal.jpg', 0)
ON DUPLICATE KEY UPDATE
    product_id = VALUES(product_id),
    sku_name = VALUES(sku_name),
    specification = VALUES(specification),
    original_price = VALUES(original_price),
    sale_price = VALUES(sale_price),
    image_url = VALUES(image_url),
    status = VALUES(status);

-- 4. 商品库存
INSERT INTO inventories
    (id, sku_id, stock_quantity, locked_quantity, warning_quantity)
VALUES
    (3001, 2001, 120, 3, 10),
    (3002, 2002, 80, 0, 10),
    (3003, 2003, 60, 5, 10),
    (3004, 2004, 200, 2, 20),
    (3005, 2005, 150, 0, 20),
    (3006, 2006, 90, 1, 10),
    (3007, 2007, 0, 0, 10)
ON DUPLICATE KEY UPDATE
    sku_id = VALUES(sku_id),
    stock_quantity = VALUES(stock_quantity),
    locked_quantity = VALUES(locked_quantity),
    warning_quantity = VALUES(warning_quantity);

-- 5. 用户收货地址
INSERT INTO user_addresses
    (id, user_id, receiver_name, receiver_phone, province, city,
     district, detail_address, is_default)
VALUES
    (4001, @buyer_id, '张三', '13800000001', '广东省', '深圳市',
     '南山区', '科技园科苑路 88 号 1 栋 1001 室', 1),
    (4002, @buyer_id, '张三', '13800000001', '北京市', '北京市',
     '朝阳区', '望京街道测试路 10 号', 0)
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id),
    receiver_name = VALUES(receiver_name),
    receiver_phone = VALUES(receiver_phone),
    province = VALUES(province),
    city = VALUES(city),
    district = VALUES(district),
    detail_address = VALUES(detail_address),
    is_default = VALUES(is_default);

-- 6. 购物车
INSERT INTO carts (id, user_id)
VALUES
    (5001, @buyer_id)
ON DUPLICATE KEY UPDATE
    user_id = VALUES(user_id);

-- 7. 购物车明细
INSERT INTO cart_items
    (id, cart_id, product_id, sku_id, quantity, selected)
VALUES
    (6001, 5001, 1002, 2003, 1, 1),
    (6002, 5001, 1004, 2006, 2, 0),
    (6003, 5001, 1001, 2002, 1, 1)
ON DUPLICATE KEY UPDATE
    cart_id = VALUES(cart_id),
    product_id = VALUES(product_id),
    sku_id = VALUES(sku_id),
    quantity = VALUES(quantity),
    selected = VALUES(selected);

-- 8. 订单
INSERT INTO orders
    (id, order_no, user_id, receiver_name, receiver_phone, receiver_address,
     total_amount, freight_amount, payable_amount, order_status,
     payment_status, delivery_status, user_remark, cancel_reason,
     paid_at, shipped_at, completed_at, cancelled_at,
     created_at, updated_at)
VALUES
    (7001, 'TEST-20261020-0001', @buyer_id, '张三', '13800000001',
     '广东省深圳市南山区科技园科苑路 88 号 1 栋 1001 室',
     338.80, 0.00, 338.80, '已完成', '已支付', '已签收',
     '请工作日送货', NULL,
     '2026-10-18 10:20:00', '2026-10-18 16:30:00',
     '2026-10-20 14:00:00', NULL,
     '2026-10-18 10:00:00', '2026-10-20 14:00:00'),
    (7002, 'TEST-20261020-0002', @buyer_id, '张三', '13800000001',
     '广东省深圳市南山区科技园科苑路 88 号 1 栋 1001 室',
     228.90, 10.00, 238.90, '待付款', '未支付', '待发货',
     NULL, NULL,
     NULL, NULL, NULL, NULL,
     '2026-10-20 09:30:00', '2026-10-20 09:30:00'),
    (7003, 'TEST-20261020-0003', @buyer_id, '张三', '13800000001',
     '北京市北京市朝阳区望京街道测试路 10 号',
     279.00, 0.00, 279.00, '已发货', '已支付', '已发货',
     '麻烦发顺丰', NULL,
     '2026-10-19 11:00:00', '2026-10-19 15:00:00',
     NULL, NULL,
     '2026-10-19 10:30:00', '2026-10-19 15:00:00')
ON DUPLICATE KEY UPDATE
    order_no = VALUES(order_no),
    user_id = VALUES(user_id),
    receiver_name = VALUES(receiver_name),
    receiver_phone = VALUES(receiver_phone),
    receiver_address = VALUES(receiver_address),
    total_amount = VALUES(total_amount),
    freight_amount = VALUES(freight_amount),
    payable_amount = VALUES(payable_amount),
    order_status = VALUES(order_status),
    payment_status = VALUES(payment_status),
    delivery_status = VALUES(delivery_status),
    user_remark = VALUES(user_remark),
    cancel_reason = VALUES(cancel_reason),
    paid_at = VALUES(paid_at),
    shipped_at = VALUES(shipped_at),
    completed_at = VALUES(completed_at),
    cancelled_at = VALUES(cancelled_at),
    updated_at = VALUES(updated_at);

-- 9. 订单明细
INSERT INTO order_items
    (id, order_id, product_id, sku_id, product_name, sku_name,
     sku_specification, product_image, unit_price, quantity, total_amount)
VALUES
    (8001, 7001, 1003, 2004, '纯棉短袖 T 恤', '黑色 L 码',
     '{"颜色":"黑色","尺码":"L"}', '/images/test/cotton-tshirt-black.jpg',
     79.90, 2, 159.80),
    (8002, 7001, 1001, 2001, '无线蓝牙耳机', '黑色',
     '{"颜色":"黑色"}', '/images/test/bluetooth-headset-black.jpg',
     179.00, 1, 179.00),
    (8003, 7002, 1004, 2006, '便携榨汁杯', '樱花粉',
     '{"颜色":"樱花粉"}', '/images/test/portable-juicer-pink.jpg',
     149.00, 1, 149.00),
    (8004, 7002, 1003, 2005, '纯棉短袖 T 恤', '白色 M 码',
     '{"颜色":"白色","尺码":"M"}', '/images/test/cotton-tshirt-white.jpg',
     79.90, 1, 79.90),
    (8005, 7003, 1002, 2003, '机械键盘', '茶轴',
     '{"轴体":"茶轴","颜色":"黑色"}', '/images/test/mechanical-keyboard-brown.jpg',
     279.00, 1, 279.00)
ON DUPLICATE KEY UPDATE
    order_id = VALUES(order_id),
    product_id = VALUES(product_id),
    sku_id = VALUES(sku_id),
    product_name = VALUES(product_name),
    sku_name = VALUES(sku_name),
    sku_specification = VALUES(sku_specification),
    product_image = VALUES(product_image),
    unit_price = VALUES(unit_price),
    quantity = VALUES(quantity),
    total_amount = VALUES(total_amount);

