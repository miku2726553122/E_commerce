-- Generated local demonstration catalog normalization, 2026-10-22.
-- Prices are illustrative CNY prices, not live market quotes.
-- Image URLs come from POST /api/upload/image; provenance is in image-manifest.json.
SET NAMES utf8mb4;
START TRANSACTION;

UPDATE products SET `product_name`='iPhone 15 Pro Max', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7bb99fe9-9fdf-46c8-be53-0c5e97a54038.jpg', `description`='iPhone 15 Pro Max · 256GB', `detail_content`='<p>iPhone 15 Pro Max</p><p>规格：256GB</p>', `base_price`=5999, version=version+1 WHERE id=1 AND product_code='PD000001';

UPDATE product_skus SET sku_name='iPhone 15 Pro Max / 256GB', specification='{"规格": "256GB"}', original_price=5999, sale_price=5999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7bb99fe9-9fdf-46c8-be53-0c5e97a54038.jpg' WHERE id=1 AND product_id=1;

UPDATE product_skus SET sku_name='iPhone 15 Pro Max / 512GB', specification='{"规格": "512GB"}', original_price=6999, sale_price=6999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7bb99fe9-9fdf-46c8-be53-0c5e97a54038.jpg' WHERE id=2 AND product_id=1;

UPDATE products SET `product_name`='华为Mate 60 Pro', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c828c855-861b-4b9e-8182-8541d4bec262.jpg', `description`='华为Mate 60 Pro · 12GB+256GB', `detail_content`='<p>华为Mate 60 Pro</p><p>规格：12GB+256GB</p>', `base_price`=4599, version=version+1 WHERE id=2 AND product_code='PD000002';

UPDATE product_skus SET sku_name='华为Mate 60 Pro / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=4599, sale_price=4599, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c828c855-861b-4b9e-8182-8541d4bec262.jpg' WHERE id=3 AND product_id=2;

UPDATE product_skus SET sku_name='华为Mate 60 Pro / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=5099, sale_price=5099, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c828c855-861b-4b9e-8182-8541d4bec262.jpg' WHERE id=4 AND product_id=2;

UPDATE products SET `product_name`='小米14 Ultra', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/1190d8ec-8bef-4ff9-9959-a820be8d5b31.jpg', `description`='小米14 Ultra · 12GB+256GB', `detail_content`='<p>小米14 Ultra</p><p>规格：12GB+256GB</p>', `base_price`=5499, version=version+1 WHERE id=3 AND product_code='PD000003';

UPDATE product_skus SET sku_name='小米14 Ultra / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=5499, sale_price=5499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/1190d8ec-8bef-4ff9-9959-a820be8d5b31.jpg' WHERE id=5 AND product_id=3;

UPDATE product_skus SET sku_name='小米14 Ultra / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=5999, sale_price=5999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/1190d8ec-8bef-4ff9-9959-a820be8d5b31.jpg' WHERE id=6 AND product_id=3;

UPDATE products SET `product_name`='OPPO Find X7', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/07c88d3c-6be8-480e-abc6-949b419ff883.jpg', `description`='OPPO Find X7 · 12GB+256GB', `detail_content`='<p>OPPO Find X7</p><p>规格：12GB+256GB</p>', `base_price`=2999, version=version+1 WHERE id=4 AND product_code='PD000004';

UPDATE product_skus SET sku_name='OPPO Find X7 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2999, sale_price=2999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/07c88d3c-6be8-480e-abc6-949b419ff883.jpg' WHERE id=7 AND product_id=4;

UPDATE product_skus SET sku_name='OPPO Find X7 / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=3499, sale_price=3499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/07c88d3c-6be8-480e-abc6-949b419ff883.jpg' WHERE id=8 AND product_id=4;

UPDATE products SET `product_name`='vivo X100 Pro', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64e1cf2b-7833-40c4-90c7-be2cca3e9760.jpg', `description`='vivo X100 Pro · 12GB+256GB', `detail_content`='<p>vivo X100 Pro</p><p>规格：12GB+256GB</p>', `base_price`=3999, version=version+1 WHERE id=5 AND product_code='PD000005';

UPDATE product_skus SET sku_name='vivo X100 Pro / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3999, sale_price=3999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64e1cf2b-7833-40c4-90c7-be2cca3e9760.jpg' WHERE id=9 AND product_id=5;

UPDATE product_skus SET sku_name='vivo X100 Pro / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=4499, sale_price=4499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64e1cf2b-7833-40c4-90c7-be2cca3e9760.jpg' WHERE id=10 AND product_id=5;

UPDATE products SET `product_name`='三星Galaxy S24', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd52b49f-94b0-4e34-a81c-292dc66b3588.jpg', `description`='三星Galaxy S24 · 8GB+256GB', `detail_content`='<p>三星Galaxy S24</p><p>规格：8GB+256GB</p>', `base_price`=3499, version=version+1 WHERE id=6 AND product_code='PD000006';

UPDATE product_skus SET sku_name='三星Galaxy S24 / 8GB+256GB', specification='{"规格": "8GB+256GB"}', original_price=3499, sale_price=3499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd52b49f-94b0-4e34-a81c-292dc66b3588.jpg' WHERE id=11 AND product_id=6;

UPDATE product_skus SET sku_name='三星Galaxy S24 / 8GB+512GB', specification='{"规格": "8GB+512GB"}', original_price=3999, sale_price=3999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd52b49f-94b0-4e34-a81c-292dc66b3588.jpg' WHERE id=12 AND product_id=6;

UPDATE products SET `product_name`='荣耀Magic6', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e508d2ae-889f-4b09-ac0f-9c3a672280ad.jpg', `description`='荣耀Magic6 · 12GB+256GB', `detail_content`='<p>荣耀Magic6</p><p>规格：12GB+256GB</p>', `base_price`=3299, version=version+1 WHERE id=7 AND product_code='PD000007';

UPDATE product_skus SET sku_name='荣耀Magic6 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3299, sale_price=3299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e508d2ae-889f-4b09-ac0f-9c3a672280ad.jpg' WHERE id=13 AND product_id=7;

UPDATE product_skus SET sku_name='荣耀Magic6 / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=3799, sale_price=3799, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e508d2ae-889f-4b09-ac0f-9c3a672280ad.jpg' WHERE id=14 AND product_id=7;

UPDATE products SET `product_name`='一加12', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/489b9d84-78ac-43b4-8dc5-78ff923ff710.jpg', `description`='一加12 · 12GB+256GB', `detail_content`='<p>一加12</p><p>规格：12GB+256GB</p>', `base_price`=2999, version=version+1 WHERE id=8 AND product_code='PD000008';

UPDATE product_skus SET sku_name='一加12 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2999, sale_price=2999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/489b9d84-78ac-43b4-8dc5-78ff923ff710.jpg' WHERE id=15 AND product_id=8;

UPDATE product_skus SET sku_name='一加12 / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=3499, sale_price=3499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/489b9d84-78ac-43b4-8dc5-78ff923ff710.jpg' WHERE id=16 AND product_id=8;

UPDATE products SET `product_name`='魅族21', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/11c5a01f-c605-4d44-98bc-21f0055d13f9.jpg', `description`='魅族21 · 12GB+256GB', `detail_content`='<p>魅族21</p><p>规格：12GB+256GB</p>', `base_price`=2299, version=version+1 WHERE id=9 AND product_code='PD000009';

UPDATE product_skus SET sku_name='魅族21 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2299, sale_price=2299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/11c5a01f-c605-4d44-98bc-21f0055d13f9.jpg' WHERE id=17 AND product_id=9;

UPDATE product_skus SET sku_name='魅族21 / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=2799, sale_price=2799, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/11c5a01f-c605-4d44-98bc-21f0055d13f9.jpg' WHERE id=18 AND product_id=9;

UPDATE products SET `product_name`='努比亚Z60 Ultra', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/4b1b033c-6f96-4eaf-a78f-6dda8afdf7b8.jpg', `description`='努比亚Z60 Ultra · 12GB+256GB', `detail_content`='<p>努比亚Z60 Ultra</p><p>规格：12GB+256GB</p>', `base_price`=3299, version=version+1 WHERE id=10 AND product_code='PD000010';

UPDATE product_skus SET sku_name='努比亚Z60 Ultra / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3299, sale_price=3299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/4b1b033c-6f96-4eaf-a78f-6dda8afdf7b8.jpg' WHERE id=19 AND product_id=10;

UPDATE product_skus SET sku_name='努比亚Z60 Ultra / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=3799, sale_price=3799, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/4b1b033c-6f96-4eaf-a78f-6dda8afdf7b8.jpg' WHERE id=20 AND product_id=10;

UPDATE products SET `product_name`='realme GT5', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4f66f93-af62-485c-b841-73caf67b8903.jpg', `description`='realme GT5 · 12GB+256GB', `detail_content`='<p>realme GT5</p><p>规格：12GB+256GB</p>', `base_price`=2299, version=version+1 WHERE id=11 AND product_code='PD000011';

UPDATE product_skus SET sku_name='realme GT5 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2299, sale_price=2299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4f66f93-af62-485c-b841-73caf67b8903.jpg' WHERE id=21 AND product_id=11;

UPDATE product_skus SET sku_name='realme GT5 / 12GB+512GB', specification='{"规格": "12GB+512GB"}', original_price=2799, sale_price=2799, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4f66f93-af62-485c-b841-73caf67b8903.jpg' WHERE id=22 AND product_id=11;

UPDATE products SET `product_name`='联想ThinkPad X1', `merchant_id`=2, `main_image`=NULL, `description`='联想ThinkPad X1 · 16GB+512GB', `detail_content`='<p>联想ThinkPad X1</p><p>规格：16GB+512GB</p>', `base_price`=9999, version=version+1 WHERE id=12 AND product_code='PD000012';

UPDATE product_skus SET sku_name='联想ThinkPad X1 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=9999, sale_price=9999, image_url=NULL WHERE id=23 AND product_id=12;

UPDATE product_skus SET sku_name='联想ThinkPad X1 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=10999, sale_price=10999, image_url=NULL WHERE id=24 AND product_id=12;

UPDATE products SET `product_name`='华为MateBook 16', `merchant_id`=2, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a8b28a91-5002-4b35-99a8-577559549235.jpg', `description`='华为MateBook 16 · 16GB+512GB', `detail_content`='<p>华为MateBook 16</p><p>规格：16GB+512GB</p>', `base_price`=5499, version=version+1 WHERE id=13 AND product_code='PD000013';

UPDATE product_skus SET sku_name='华为MateBook 16 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=5499, sale_price=5499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a8b28a91-5002-4b35-99a8-577559549235.jpg' WHERE id=25 AND product_id=13;

UPDATE product_skus SET sku_name='华为MateBook 16 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=6499, sale_price=6499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a8b28a91-5002-4b35-99a8-577559549235.jpg' WHERE id=26 AND product_id=13;

UPDATE products SET `product_name`='小米Book Pro', `merchant_id`=2, `main_image`=NULL, `description`='小米Book Pro · 16GB+512GB', `detail_content`='<p>小米Book Pro</p><p>规格：16GB+512GB</p>', `base_price`=4999, version=version+1 WHERE id=14 AND product_code='PD000014';

UPDATE product_skus SET sku_name='小米Book Pro / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=4999, sale_price=4999, image_url=NULL WHERE id=27 AND product_id=14;

UPDATE product_skus SET sku_name='小米Book Pro / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=5999, sale_price=5999, image_url=NULL WHERE id=28 AND product_id=14;

UPDATE products SET `product_name`='戴尔XPS 15', `merchant_id`=2, `main_image`=NULL, `description`='戴尔XPS 15 · 16GB+512GB', `detail_content`='<p>戴尔XPS 15</p><p>规格：16GB+512GB</p>', `base_price`=11999, version=version+1 WHERE id=15 AND product_code='PD000015';

UPDATE product_skus SET sku_name='戴尔XPS 15 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=11999, sale_price=11999, image_url=NULL WHERE id=29 AND product_id=15;

UPDATE product_skus SET sku_name='戴尔XPS 15 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=12999, sale_price=12999, image_url=NULL WHERE id=30 AND product_id=15;

UPDATE products SET `product_name`='惠普暗影精灵9', `merchant_id`=2, `main_image`=NULL, `description`='惠普暗影精灵9 · 16GB+512GB', `detail_content`='<p>惠普暗影精灵9</p><p>规格：16GB+512GB</p>', `base_price`=6499, version=version+1 WHERE id=16 AND product_code='PD000016';

UPDATE product_skus SET sku_name='惠普暗影精灵9 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=6499, sale_price=6499, image_url=NULL WHERE id=31 AND product_id=16;

UPDATE product_skus SET sku_name='惠普暗影精灵9 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=7499, sale_price=7499, image_url=NULL WHERE id=32 AND product_id=16;

UPDATE products SET `product_name`='华硕天选4', `merchant_id`=2, `main_image`=NULL, `description`='华硕天选4 · 16GB+512GB', `detail_content`='<p>华硕天选4</p><p>规格：16GB+512GB</p>', `base_price`=5999, version=version+1 WHERE id=17 AND product_code='PD000017';

UPDATE product_skus SET sku_name='华硕天选4 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=5999, sale_price=5999, image_url=NULL WHERE id=33 AND product_id=17;

UPDATE product_skus SET sku_name='华硕天选4 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=6999, sale_price=6999, image_url=NULL WHERE id=34 AND product_id=17;

UPDATE products SET `product_name`='宏碁掠夺者', `merchant_id`=2, `main_image`=NULL, `description`='宏碁掠夺者 · 16GB+512GB', `detail_content`='<p>宏碁掠夺者</p><p>规格：16GB+512GB</p>', `base_price`=6999, version=version+1 WHERE id=18 AND product_code='PD000018';

UPDATE product_skus SET sku_name='宏碁掠夺者 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=6999, sale_price=6999, image_url=NULL WHERE id=35 AND product_id=18;

UPDATE product_skus SET sku_name='宏碁掠夺者 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=7999, sale_price=7999, image_url=NULL WHERE id=36 AND product_id=18;

UPDATE products SET `product_name`='机械革命蛟龙16', `merchant_id`=2, `main_image`=NULL, `description`='机械革命蛟龙16 · 16GB+512GB', `detail_content`='<p>机械革命蛟龙16</p><p>规格：16GB+512GB</p>', `base_price`=5499, version=version+1 WHERE id=19 AND product_code='PD000019';

UPDATE product_skus SET sku_name='机械革命蛟龙16 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=5499, sale_price=5499, image_url=NULL WHERE id=37 AND product_id=19;

UPDATE product_skus SET sku_name='机械革命蛟龙16 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=6499, sale_price=6499, image_url=NULL WHERE id=38 AND product_id=19;

UPDATE products SET `product_name`='荣耀MagicBook', `merchant_id`=2, `main_image`=NULL, `description`='荣耀MagicBook · 16GB+512GB', `detail_content`='<p>荣耀MagicBook</p><p>规格：16GB+512GB</p>', `base_price`=3999, version=version+1 WHERE id=20 AND product_code='PD000020';

UPDATE product_skus SET sku_name='荣耀MagicBook / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=3999, sale_price=3999, image_url=NULL WHERE id=39 AND product_id=20;

UPDATE product_skus SET sku_name='荣耀MagicBook / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=4999, sale_price=4999, image_url=NULL WHERE id=40 AND product_id=20;

UPDATE products SET `product_name`='RedmiBook Pro', `merchant_id`=2, `main_image`=NULL, `description`='RedmiBook Pro · 16GB+512GB', `detail_content`='<p>RedmiBook Pro</p><p>规格：16GB+512GB</p>', `base_price`=4499, version=version+1 WHERE id=21 AND product_code='PD000021';

UPDATE product_skus SET sku_name='RedmiBook Pro / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=4499, sale_price=4499, image_url=NULL WHERE id=41 AND product_id=21;

UPDATE product_skus SET sku_name='RedmiBook Pro / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=5499, sale_price=5499, image_url=NULL WHERE id=42 AND product_id=21;

UPDATE products SET `product_name`='神舟战神Z8', `merchant_id`=2, `main_image`=NULL, `description`='神舟战神Z8 · 16GB+512GB', `detail_content`='<p>神舟战神Z8</p><p>规格：16GB+512GB</p>', `base_price`=5299, version=version+1 WHERE id=22 AND product_code='PD000022';

UPDATE product_skus SET sku_name='神舟战神Z8 / 16GB+512GB', specification='{"规格": "16GB+512GB"}', original_price=5299, sale_price=5299, image_url=NULL WHERE id=43 AND product_id=22;

UPDATE product_skus SET sku_name='神舟战神Z8 / 32GB+1TB', specification='{"规格": "32GB+1TB"}', original_price=6299, sale_price=6299, image_url=NULL WHERE id=44 AND product_id=22;

UPDATE products SET `product_name`='苹果MagSafe手机壳', `merchant_id`=2, `main_image`=NULL, `description`='苹果MagSafe手机壳 · iPhone 15 Pro Max 适用 / 透明', `detail_content`='<p>苹果MagSafe手机壳</p><p>规格：iPhone 15 Pro Max 适用 / 透明</p>', `base_price`=299, version=version+1 WHERE id=23 AND product_code='PD000023';

UPDATE product_skus SET sku_name='苹果MagSafe手机壳 / iPhone 15 Pro Max 适用 / 透明', specification='{"规格": "iPhone 15 Pro Max 适用 / 透明"}', original_price=299, sale_price=299, image_url=NULL WHERE id=45 AND product_id=23;

UPDATE product_skus SET sku_name='苹果MagSafe手机壳 / iPhone 15 Pro Max 适用 / 黑色硅胶', specification='{"规格": "iPhone 15 Pro Max 适用 / 黑色硅胶"}', original_price=399, sale_price=399, image_url=NULL WHERE id=46 AND product_id=23;

UPDATE products SET `product_name`='华为保护壳', `merchant_id`=2, `main_image`=NULL, `description`='华为保护壳 · Mate 60 Pro 适用 / 黑色', `detail_content`='<p>华为保护壳</p><p>规格：Mate 60 Pro 适用 / 黑色</p>', `base_price`=99, version=version+1 WHERE id=24 AND product_code='PD000024';

UPDATE product_skus SET sku_name='华为保护壳 / Mate 60 Pro 适用 / 黑色', specification='{"规格": "Mate 60 Pro 适用 / 黑色"}', original_price=99, sale_price=99, image_url=NULL WHERE id=47 AND product_id=24;

UPDATE product_skus SET sku_name='华为保护壳 / Mate 60 Pro 适用 / 蓝色', specification='{"规格": "Mate 60 Pro 适用 / 蓝色"}', original_price=99, sale_price=99, image_url=NULL WHERE id=48 AND product_id=24;

UPDATE products SET `product_name`='小米移动电源', `merchant_id`=2, `main_image`=NULL, `description`='小米移动电源 · 10000mAh / 22.5W', `detail_content`='<p>小米移动电源</p><p>规格：10000mAh / 22.5W</p>', `base_price`=129, version=version+1 WHERE id=25 AND product_code='PD000025';

UPDATE product_skus SET sku_name='小米移动电源 / 10000mAh / 22.5W', specification='{"规格": "10000mAh / 22.5W"}', original_price=129, sale_price=129, image_url=NULL WHERE id=49 AND product_id=25;

UPDATE product_skus SET sku_name='小米移动电源 / 20000mAh / 33W', specification='{"规格": "20000mAh / 33W"}', original_price=199, sale_price=199, image_url=NULL WHERE id=50 AND product_id=25;

UPDATE products SET `product_name`='品胜数据线', `merchant_id`=2, `main_image`=NULL, `description`='品胜数据线 · USB-C / 1米', `detail_content`='<p>品胜数据线</p><p>规格：USB-C / 1米</p>', `base_price`=19.9, version=version+1 WHERE id=26 AND product_code='PD000026';

UPDATE product_skus SET sku_name='品胜数据线 / USB-C / 1米', specification='{"规格": "USB-C / 1米"}', original_price=19.9, sale_price=19.9, image_url=NULL WHERE id=51 AND product_id=26;

UPDATE product_skus SET sku_name='品胜数据线 / USB-C / 2米', specification='{"规格": "USB-C / 2米"}', original_price=29.9, sale_price=29.9, image_url=NULL WHERE id=52 AND product_id=26;

UPDATE products SET `product_name`='绿联充电器', `merchant_id`=2, `main_image`=NULL, `description`='绿联充电器 · 65W / USB-C', `detail_content`='<p>绿联充电器</p><p>规格：65W / USB-C</p>', `base_price`=99, version=version+1 WHERE id=27 AND product_code='PD000027';

UPDATE product_skus SET sku_name='绿联充电器 / 65W / USB-C', specification='{"规格": "65W / USB-C"}', original_price=99, sale_price=99, image_url=NULL WHERE id=53 AND product_id=27;

UPDATE product_skus SET sku_name='绿联充电器 / 100W / USB-C', specification='{"规格": "100W / USB-C"}', original_price=169, sale_price=169, image_url=NULL WHERE id=54 AND product_id=27;

UPDATE products SET `product_name`='倍思车载支架', `merchant_id`=2, `main_image`=NULL, `description`='倍思车载支架 · 出风口款', `detail_content`='<p>倍思车载支架</p><p>规格：出风口款</p>', `base_price`=59, version=version+1 WHERE id=28 AND product_code='PD000028';

UPDATE product_skus SET sku_name='倍思车载支架 / 出风口款', specification='{"规格": "出风口款"}', original_price=59, sale_price=59, image_url=NULL WHERE id=55 AND product_id=28;

UPDATE product_skus SET sku_name='倍思车载支架 / 中控台款', specification='{"规格": "中控台款"}', original_price=69, sale_price=69, image_url=NULL WHERE id=56 AND product_id=28;

UPDATE products SET `product_name`='摩米士蓝牙耳机', `merchant_id`=2, `main_image`=NULL, `description`='摩米士蓝牙耳机 · 真无线 / 白色', `detail_content`='<p>摩米士蓝牙耳机</p><p>规格：真无线 / 白色</p>', `base_price`=199, version=version+1 WHERE id=29 AND product_code='PD000029';

UPDATE product_skus SET sku_name='摩米士蓝牙耳机 / 真无线 / 白色', specification='{"规格": "真无线 / 白色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=57 AND product_id=29;

UPDATE product_skus SET sku_name='摩米士蓝牙耳机 / 真无线 / 黑色', specification='{"规格": "真无线 / 黑色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=58 AND product_id=29;

UPDATE products SET `product_name`='南卡Runner Pro', `merchant_id`=2, `main_image`=NULL, `description`='南卡Runner Pro · 骨传导 / 黑色', `detail_content`='<p>南卡Runner Pro</p><p>规格：骨传导 / 黑色</p>', `base_price`=799, version=version+1 WHERE id=30 AND product_code='PD000030';

UPDATE product_skus SET sku_name='南卡Runner Pro / 骨传导 / 黑色', specification='{"规格": "骨传导 / 黑色"}', original_price=799, sale_price=799, image_url=NULL WHERE id=59 AND product_id=30;

UPDATE product_skus SET sku_name='南卡Runner Pro / 骨传导 / 灰色', specification='{"规格": "骨传导 / 灰色"}', original_price=799, sale_price=799, image_url=NULL WHERE id=60 AND product_id=30;

UPDATE products SET `product_name`='漫步者 LolliPods', `merchant_id`=2, `main_image`=NULL, `description`='漫步者 LolliPods · 真无线 / 白色', `detail_content`='<p>漫步者 LolliPods</p><p>规格：真无线 / 白色</p>', `base_price`=199, version=version+1 WHERE id=31 AND product_code='PD000031';

UPDATE product_skus SET sku_name='漫步者 LolliPods / 真无线 / 白色', specification='{"规格": "真无线 / 白色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=61 AND product_id=31;

UPDATE products SET `product_name`='JBL TUNE 770NC', `merchant_id`=2, `main_image`=NULL, `description`='JBL TUNE 770NC · 头戴式 / 黑色', `detail_content`='<p>JBL TUNE 770NC</p><p>规格：头戴式 / 黑色</p>', `base_price`=499, version=version+1 WHERE id=32 AND product_code='PD000032';

UPDATE product_skus SET sku_name='JBL TUNE 770NC / 头戴式 / 黑色', specification='{"规格": "头戴式 / 黑色"}', original_price=499, sale_price=499, image_url=NULL WHERE id=62 AND product_id=32;

UPDATE products SET `product_name`='索尼WH-1000XM5', `merchant_id`=2, `main_image`=NULL, `description`='索尼WH-1000XM5 · 头戴式 / 黑色', `detail_content`='<p>索尼WH-1000XM5</p><p>规格：头戴式 / 黑色</p>', `base_price`=1799, version=version+1 WHERE id=33 AND product_code='PD000033';

UPDATE product_skus SET sku_name='索尼WH-1000XM5 / 头戴式 / 黑色', specification='{"规格": "头戴式 / 黑色"}', original_price=1799, sale_price=1799, image_url=NULL WHERE id=63 AND product_id=33;

UPDATE products SET `product_name`='纯棉T恤男', `merchant_id`=3, `main_image`=NULL, `description`='纯棉T恤男 · M码 / 白色', `detail_content`='<p>纯棉T恤男</p><p>规格：M码 / 白色</p>', `base_price`=59, version=version+1 WHERE id=34 AND product_code='PD000034';

UPDATE product_skus SET sku_name='纯棉T恤男 / M码 / 白色', specification='{"规格": "M码 / 白色"}', original_price=59, sale_price=59, image_url=NULL WHERE id=64 AND product_id=34;

UPDATE products SET `product_name`='商务休闲西裤', `merchant_id`=3, `main_image`=NULL, `description`='商务休闲西裤 · 30码 / 黑色', `detail_content`='<p>商务休闲西裤</p><p>规格：30码 / 黑色</p>', `base_price`=129, version=version+1 WHERE id=35 AND product_code='PD000035';

UPDATE product_skus SET sku_name='商务休闲西裤 / 30码 / 黑色', specification='{"规格": "30码 / 黑色"}', original_price=129, sale_price=129, image_url=NULL WHERE id=65 AND product_id=35;

UPDATE products SET `product_name`='男士羽绒服', `merchant_id`=3, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/078ca86a-f6b3-4a1b-ac0f-bb19505b5996.jpg', `description`='男士羽绒服 · L码 / 深绿色', `detail_content`='<p>男士羽绒服</p><p>规格：L码 / 深绿色</p>', `base_price`=499, version=version+1 WHERE id=36 AND product_code='PD000036';

UPDATE product_skus SET sku_name='男士羽绒服 / L码 / 深绿色', specification='{"规格": "L码 / 深绿色"}', original_price=499, sale_price=499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/078ca86a-f6b3-4a1b-ac0f-bb19505b5996.jpg' WHERE id=66 AND product_id=36;

UPDATE products SET `product_name`='男士牛仔裤', `merchant_id`=3, `main_image`=NULL, `description`='男士牛仔裤 · 30码 / 蓝色', `detail_content`='<p>男士牛仔裤</p><p>规格：30码 / 蓝色</p>', `base_price`=159, version=version+1 WHERE id=37 AND product_code='PD000037';

UPDATE product_skus SET sku_name='男士牛仔裤 / 30码 / 蓝色', specification='{"规格": "30码 / 蓝色"}', original_price=159, sale_price=159, image_url=NULL WHERE id=67 AND product_id=37;

UPDATE products SET `product_name`='休闲夹克', `merchant_id`=3, `main_image`=NULL, `description`='休闲夹克 · L码 / 黑色', `detail_content`='<p>休闲夹克</p><p>规格：L码 / 黑色</p>', `base_price`=229, version=version+1 WHERE id=38 AND product_code='PD000038';

UPDATE product_skus SET sku_name='休闲夹克 / L码 / 黑色', specification='{"规格": "L码 / 黑色"}', original_price=229, sale_price=229, image_url=NULL WHERE id=68 AND product_id=38;

UPDATE products SET `product_name`='男士卫衣', `merchant_id`=3, `main_image`=NULL, `description`='男士卫衣 · L码 / 灰色', `detail_content`='<p>男士卫衣</p><p>规格：L码 / 灰色</p>', `base_price`=129, version=version+1 WHERE id=39 AND product_code='PD000039';

UPDATE product_skus SET sku_name='男士卫衣 / L码 / 灰色', specification='{"规格": "L码 / 灰色"}', original_price=129, sale_price=129, image_url=NULL WHERE id=69 AND product_id=39;

UPDATE products SET `product_name`='POLO衫男', `merchant_id`=3, `main_image`=NULL, `description`='POLO衫男 · L码 / 白色', `detail_content`='<p>POLO衫男</p><p>规格：L码 / 白色</p>', `base_price`=99, version=version+1 WHERE id=40 AND product_code='PD000040';

UPDATE product_skus SET sku_name='POLO衫男 / L码 / 白色', specification='{"规格": "L码 / 白色"}', original_price=99, sale_price=99, image_url=NULL WHERE id=70 AND product_id=40;

UPDATE products SET `product_name`='男士风衣', `merchant_id`=3, `main_image`=NULL, `description`='男士风衣 · L码 / 卡其色', `detail_content`='<p>男士风衣</p><p>规格：L码 / 卡其色</p>', `base_price`=329, version=version+1 WHERE id=41 AND product_code='PD000041';

UPDATE product_skus SET sku_name='男士风衣 / L码 / 卡其色', specification='{"规格": "L码 / 卡其色"}', original_price=329, sale_price=329, image_url=NULL WHERE id=71 AND product_id=41;

UPDATE products SET `product_name`='运动短裤男', `merchant_id`=3, `main_image`=NULL, `description`='运动短裤男 · L码 / 黑色', `detail_content`='<p>运动短裤男</p><p>规格：L码 / 黑色</p>', `base_price`=69, version=version+1 WHERE id=42 AND product_code='PD000042';

UPDATE product_skus SET sku_name='运动短裤男 / L码 / 黑色', specification='{"规格": "L码 / 黑色"}', original_price=69, sale_price=69, image_url=NULL WHERE id=72 AND product_id=42;

UPDATE products SET `product_name`='男士衬衫', `merchant_id`=3, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/eb6aa496-6949-49f7-8644-1fd77f3fac5c.jpg', `description`='男士衬衫 · L码 / 灰色', `detail_content`='<p>男士衬衫</p><p>规格：L码 / 灰色</p>', `base_price`=129, version=version+1 WHERE id=43 AND product_code='PD000043';

UPDATE product_skus SET sku_name='男士衬衫 / L码 / 灰色', specification='{"规格": "L码 / 灰色"}', original_price=129, sale_price=129, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/eb6aa496-6949-49f7-8644-1fd77f3fac5c.jpg' WHERE id=73 AND product_id=43;

UPDATE products SET `product_name`='男士毛衣', `merchant_id`=3, `main_image`=NULL, `description`='男士毛衣 · L码 / 灰色', `detail_content`='<p>男士毛衣</p><p>规格：L码 / 灰色</p>', `base_price`=199, version=version+1 WHERE id=44 AND product_code='PD000044';

UPDATE product_skus SET sku_name='男士毛衣 / L码 / 灰色', specification='{"规格": "L码 / 灰色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=74 AND product_id=44;

UPDATE products SET `product_name`='连衣裙女', `merchant_id`=3, `main_image`=NULL, `description`='连衣裙女 · M码 / 黑色', `detail_content`='<p>连衣裙女</p><p>规格：M码 / 黑色</p>', `base_price`=199, version=version+1 WHERE id=45 AND product_code='PD000045';

UPDATE product_skus SET sku_name='连衣裙女 / M码 / 黑色', specification='{"规格": "M码 / 黑色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=75 AND product_id=45;

UPDATE products SET `product_name`='女士针织衫', `merchant_id`=3, `main_image`=NULL, `description`='女士针织衫 · M码 / 米白色', `detail_content`='<p>女士针织衫</p><p>规格：M码 / 米白色</p>', `base_price`=139, version=version+1 WHERE id=46 AND product_code='PD000046';

UPDATE product_skus SET sku_name='女士针织衫 / M码 / 米白色', specification='{"规格": "M码 / 米白色"}', original_price=139, sale_price=139, image_url=NULL WHERE id=76 AND product_id=46;

UPDATE products SET `product_name`='半身裙女', `merchant_id`=3, `main_image`=NULL, `description`='半身裙女 · M码 / 黑色', `detail_content`='<p>半身裙女</p><p>规格：M码 / 黑色</p>', `base_price`=129, version=version+1 WHERE id=47 AND product_code='PD000047';

UPDATE product_skus SET sku_name='半身裙女 / M码 / 黑色', specification='{"规格": "M码 / 黑色"}', original_price=129, sale_price=129, image_url=NULL WHERE id=77 AND product_id=47;

UPDATE products SET `product_name`='女士风衣', `merchant_id`=3, `main_image`=NULL, `description`='女士风衣 · M码 / 卡其色', `detail_content`='<p>女士风衣</p><p>规格：M码 / 卡其色</p>', `base_price`=329, version=version+1 WHERE id=48 AND product_code='PD000048';

UPDATE product_skus SET sku_name='女士风衣 / M码 / 卡其色', specification='{"规格": "M码 / 卡其色"}', original_price=329, sale_price=329, image_url=NULL WHERE id=78 AND product_id=48;

UPDATE products SET `product_name`='雪纺衫女', `merchant_id`=3, `main_image`=NULL, `description`='雪纺衫女 · M码 / 白色', `detail_content`='<p>雪纺衫女</p><p>规格：M码 / 白色</p>', `base_price`=99, version=version+1 WHERE id=49 AND product_code='PD000049';

UPDATE product_skus SET sku_name='雪纺衫女 / M码 / 白色', specification='{"规格": "M码 / 白色"}', original_price=99, sale_price=99, image_url=NULL WHERE id=79 AND product_id=49;

UPDATE products SET `product_name`='女士大衣', `merchant_id`=3, `main_image`=NULL, `description`='女士大衣 · M码 / 驼色', `detail_content`='<p>女士大衣</p><p>规格：M码 / 驼色</p>', `base_price`=599, version=version+1 WHERE id=50 AND product_code='PD000050';

UPDATE product_skus SET sku_name='女士大衣 / M码 / 驼色', specification='{"规格": "M码 / 驼色"}', original_price=599, sale_price=599, image_url=NULL WHERE id=80 AND product_id=50;

UPDATE products SET `product_name`='阔腿裤女', `merchant_id`=3, `main_image`=NULL, `description`='阔腿裤女 · M码 / 黑色', `detail_content`='<p>阔腿裤女</p><p>规格：M码 / 黑色</p>', `base_price`=149, version=version+1 WHERE id=51 AND product_code='PD000051';

UPDATE product_skus SET sku_name='阔腿裤女 / M码 / 黑色', specification='{"规格": "M码 / 黑色"}', original_price=149, sale_price=149, image_url=NULL WHERE id=81 AND product_id=51;

UPDATE products SET `product_name`='女士卫衣', `merchant_id`=3, `main_image`=NULL, `description`='女士卫衣 · M码 / 灰色', `detail_content`='<p>女士卫衣</p><p>规格：M码 / 灰色</p>', `base_price`=139, version=version+1 WHERE id=52 AND product_code='PD000052';

UPDATE product_skus SET sku_name='女士卫衣 / M码 / 灰色', specification='{"规格": "M码 / 灰色"}', original_price=139, sale_price=139, image_url=NULL WHERE id=82 AND product_id=52;

UPDATE products SET `product_name`='A字裙女', `merchant_id`=3, `main_image`=NULL, `description`='A字裙女 · M码 / 黑色', `detail_content`='<p>A字裙女</p><p>规格：M码 / 黑色</p>', `base_price`=119, version=version+1 WHERE id=53 AND product_code='PD000053';

UPDATE product_skus SET sku_name='A字裙女 / M码 / 黑色', specification='{"规格": "M码 / 黑色"}', original_price=119, sale_price=119, image_url=NULL WHERE id=83 AND product_id=53;

UPDATE products SET `product_name`='女士毛衣', `merchant_id`=3, `main_image`=NULL, `description`='女士毛衣 · M码 / 米白色', `detail_content`='<p>女士毛衣</p><p>规格：M码 / 米白色</p>', `base_price`=199, version=version+1 WHERE id=54 AND product_code='PD000054';

UPDATE product_skus SET sku_name='女士毛衣 / M码 / 米白色', specification='{"规格": "M码 / 米白色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=84 AND product_id=54;

UPDATE products SET `product_name`='碎花裙女', `merchant_id`=3, `main_image`=NULL, `description`='碎花裙女 · M码 / 碎花', `detail_content`='<p>碎花裙女</p><p>规格：M码 / 碎花</p>', `base_price`=179, version=version+1 WHERE id=55 AND product_code='PD000055';

UPDATE product_skus SET sku_name='碎花裙女 / M码 / 碎花', specification='{"规格": "M码 / 碎花"}', original_price=179, sale_price=179, image_url=NULL WHERE id=85 AND product_id=55;

UPDATE products SET `product_name`='男士跑步鞋', `merchant_id`=3, `main_image`=NULL, `description`='男士跑步鞋 · 42码', `detail_content`='<p>男士跑步鞋</p><p>规格：42码</p>', `base_price`=199, version=version+1 WHERE id=56 AND product_code='PD000056';

UPDATE product_skus SET sku_name='男士跑步鞋 / 42码', specification='{"规格": "42码"}', original_price=199, sale_price=199, image_url=NULL WHERE id=86 AND product_id=56;

UPDATE products SET `product_name`='女士运动鞋', `merchant_id`=3, `main_image`=NULL, `description`='女士运动鞋 · 38码', `detail_content`='<p>女士运动鞋</p><p>规格：38码</p>', `base_price`=199, version=version+1 WHERE id=57 AND product_code='PD000057';

UPDATE product_skus SET sku_name='女士运动鞋 / 38码', specification='{"规格": "38码"}', original_price=199, sale_price=199, image_url=NULL WHERE id=87 AND product_id=57;

UPDATE products SET `product_name`='休闲帆布鞋', `merchant_id`=3, `main_image`=NULL, `description`='休闲帆布鞋 · 42码', `detail_content`='<p>休闲帆布鞋</p><p>规格：42码</p>', `base_price`=89, version=version+1 WHERE id=58 AND product_code='PD000058';

UPDATE product_skus SET sku_name='休闲帆布鞋 / 42码', specification='{"规格": "42码"}', original_price=89, sale_price=89, image_url=NULL WHERE id=88 AND product_id=58;

UPDATE products SET `product_name`='商务皮鞋', `merchant_id`=3, `main_image`=NULL, `description`='商务皮鞋 · 42码', `detail_content`='<p>商务皮鞋</p><p>规格：42码</p>', `base_price`=259, version=version+1 WHERE id=59 AND product_code='PD000059';

UPDATE product_skus SET sku_name='商务皮鞋 / 42码', specification='{"规格": "42码"}', original_price=259, sale_price=259, image_url=NULL WHERE id=89 AND product_id=59;

UPDATE products SET `product_name`='女士高跟鞋', `merchant_id`=3, `main_image`=NULL, `description`='女士高跟鞋 · 38码', `detail_content`='<p>女士高跟鞋</p><p>规格：38码</p>', `base_price`=199, version=version+1 WHERE id=60 AND product_code='PD000060';

UPDATE product_skus SET sku_name='女士高跟鞋 / 38码', specification='{"规格": "38码"}', original_price=199, sale_price=199, image_url=NULL WHERE id=90 AND product_id=60;

UPDATE products SET `product_name`='马丁靴', `merchant_id`=3, `main_image`=NULL, `description`='马丁靴 · 38码', `detail_content`='<p>马丁靴</p><p>规格：38码</p>', `base_price`=239, version=version+1 WHERE id=61 AND product_code='PD000061';

UPDATE product_skus SET sku_name='马丁靴 / 38码', specification='{"规格": "38码"}', original_price=239, sale_price=239, image_url=NULL WHERE id=91 AND product_id=61;

UPDATE products SET `product_name`='女士平底鞋', `merchant_id`=3, `main_image`=NULL, `description`='女士平底鞋 · 38码', `detail_content`='<p>女士平底鞋</p><p>规格：38码</p>', `base_price`=129, version=version+1 WHERE id=62 AND product_code='PD000062';

UPDATE product_skus SET sku_name='女士平底鞋 / 38码', specification='{"规格": "38码"}', original_price=129, sale_price=129, image_url=NULL WHERE id=92 AND product_id=62;

UPDATE products SET `product_name`='运动凉鞋', `merchant_id`=3, `main_image`=NULL, `description`='运动凉鞋 · 42码', `detail_content`='<p>运动凉鞋</p><p>规格：42码</p>', `base_price`=99, version=version+1 WHERE id=63 AND product_code='PD000063';

UPDATE product_skus SET sku_name='运动凉鞋 / 42码', specification='{"规格": "42码"}', original_price=99, sale_price=99, image_url=NULL WHERE id=93 AND product_id=63;

UPDATE products SET `product_name`='老爹鞋', `merchant_id`=3, `main_image`=NULL, `description`='老爹鞋 · 38码', `detail_content`='<p>老爹鞋</p><p>规格：38码</p>', `base_price`=199, version=version+1 WHERE id=64 AND product_code='PD000064';

UPDATE product_skus SET sku_name='老爹鞋 / 38码', specification='{"规格": "38码"}', original_price=199, sale_price=199, image_url=NULL WHERE id=94 AND product_id=64;

UPDATE products SET `product_name`='女士靴子', `merchant_id`=3, `main_image`=NULL, `description`='女士靴子 · 38码', `detail_content`='<p>女士靴子</p><p>规格：38码</p>', `base_price`=299, version=version+1 WHERE id=65 AND product_code='PD000065';

UPDATE product_skus SET sku_name='女士靴子 / 38码', specification='{"规格": "38码"}', original_price=299, sale_price=299, image_url=NULL WHERE id=95 AND product_id=65;

UPDATE products SET `product_name`='男士板鞋', `merchant_id`=3, `main_image`=NULL, `description`='男士板鞋 · 42码', `detail_content`='<p>男士板鞋</p><p>规格：42码</p>', `base_price`=159, version=version+1 WHERE id=66 AND product_code='PD000066';

UPDATE product_skus SET sku_name='男士板鞋 / 42码', specification='{"规格": "42码"}', original_price=159, sale_price=159, image_url=NULL WHERE id=96 AND product_id=66;

UPDATE products SET `product_name`='三只松鼠坚果大礼包', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/41e589b2-2f26-4564-aa4f-a3c1f78913e0.jpg', `description`='三只松鼠坚果大礼包 · 混合坚果大礼包 / 650g', `detail_content`='<p>三只松鼠坚果大礼包</p><p>规格：混合坚果大礼包 / 650g</p>', `base_price`=59.9, version=version+1 WHERE id=67 AND product_code='PD000067';

UPDATE product_skus SET sku_name='三只松鼠坚果大礼包 / 混合坚果大礼包 / 650g', specification='{"规格": "混合坚果大礼包 / 650g"}', original_price=59.9, sale_price=59.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/41e589b2-2f26-4564-aa4f-a3c1f78913e0.jpg' WHERE id=97 AND product_id=67;

UPDATE products SET `product_name`='良品铺子芒果干', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a1cd5d1f-d88b-49bc-b274-1656bdb9e6f2.jpg', `description`='良品铺子芒果干 · 芒果干 / 108g', `detail_content`='<p>良品铺子芒果干</p><p>规格：芒果干 / 108g</p>', `base_price`=19.9, version=version+1 WHERE id=68 AND product_code='PD000068';

UPDATE product_skus SET sku_name='良品铺子芒果干 / 芒果干 / 108g', specification='{"规格": "芒果干 / 108g"}', original_price=19.9, sale_price=19.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a1cd5d1f-d88b-49bc-b274-1656bdb9e6f2.jpg' WHERE id=98 AND product_id=68;

UPDATE products SET `product_name`='百草味手撕面包', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e7d033f4-08d7-4d2e-8517-47c4032b8de9.jpg', `description`='百草味手撕面包 · 手撕面包 / 280g', `detail_content`='<p>百草味手撕面包</p><p>规格：手撕面包 / 280g</p>', `base_price`=12.9, version=version+1 WHERE id=69 AND product_code='PD000069';

UPDATE product_skus SET sku_name='百草味手撕面包 / 手撕面包 / 280g', specification='{"规格": "手撕面包 / 280g"}', original_price=12.9, sale_price=12.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e7d033f4-08d7-4d2e-8517-47c4032b8de9.jpg' WHERE id=99 AND product_id=69;

UPDATE products SET `product_name`='洽洽每日坚果', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cee72b85-9903-4bd3-acfe-9becd7e0c170.jpg', `description`='洽洽每日坚果 · 小黄袋 / 15g×30袋 / 450g', `detail_content`='<p>洽洽每日坚果</p><p>规格：小黄袋 / 15g×30袋 / 450g</p>', `base_price`=39.9, version=version+1 WHERE id=70 AND product_code='PD000070';

UPDATE product_skus SET sku_name='洽洽每日坚果 / 小黄袋 / 15g×30袋 / 450g', specification='{"规格": "小黄袋 / 15g×30袋 / 450g"}', original_price=39.9, sale_price=39.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cee72b85-9903-4bd3-acfe-9becd7e0c170.jpg' WHERE id=100 AND product_id=70;

UPDATE products SET `product_name`='乐事薯片', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/52059ec7-d91a-4f16-a84e-206ab9a08dd1.jpg', `description`='乐事薯片 · 意大利香浓红烩味 / 70g', `detail_content`='<p>乐事薯片</p><p>规格：意大利香浓红烩味 / 70g</p>', `base_price`=7.9, version=version+1 WHERE id=71 AND product_code='PD000071';

UPDATE product_skus SET sku_name='乐事薯片 / 意大利香浓红烩味 / 70g', specification='{"规格": "意大利香浓红烩味 / 70g"}', original_price=7.9, sale_price=7.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/52059ec7-d91a-4f16-a84e-206ab9a08dd1.jpg' WHERE id=101 AND product_id=71;

UPDATE products SET `product_name`='奥利奥饼干', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/08d6fbae-bce3-4362-a20c-91c8d3eaa277.jpg', `description`='奥利奥饼干 · 草莓奶油味 / 100g', `detail_content`='<p>奥利奥饼干</p><p>规格：草莓奶油味 / 100g</p>', `base_price`=9.9, version=version+1 WHERE id=72 AND product_code='PD000072';

UPDATE product_skus SET sku_name='奥利奥饼干 / 草莓奶油味 / 100g', specification='{"规格": "草莓奶油味 / 100g"}', original_price=9.9, sale_price=9.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/08d6fbae-bce3-4362-a20c-91c8d3eaa277.jpg' WHERE id=102 AND product_id=72;

UPDATE products SET `product_name`='旺旺雪饼', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4e719da-54d0-4e25-9daf-3c6b99ac68ef.jpg', `description`='旺旺雪饼 · 雪饼 / 520g', `detail_content`='<p>旺旺雪饼</p><p>规格：雪饼 / 520g</p>', `base_price`=16.9, version=version+1 WHERE id=73 AND product_code='PD000073';

UPDATE product_skus SET sku_name='旺旺雪饼 / 雪饼 / 520g', specification='{"规格": "雪饼 / 520g"}', original_price=16.9, sale_price=16.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4e719da-54d0-4e25-9daf-3c6b99ac68ef.jpg' WHERE id=103 AND product_id=73;

UPDATE products SET `product_name`='达利园蛋黄派', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7b78ddfb-444a-4a7e-ae75-05de88dc84e7.jpg', `description`='达利园蛋黄派 · 蛋黄派 / 20g×10袋 / 200g', `detail_content`='<p>达利园蛋黄派</p><p>规格：蛋黄派 / 20g×10袋 / 200g</p>', `base_price`=8.9, version=version+1 WHERE id=74 AND product_code='PD000074';

UPDATE product_skus SET sku_name='达利园蛋黄派 / 蛋黄派 / 20g×10袋 / 200g', specification='{"规格": "蛋黄派 / 20g×10袋 / 200g"}', original_price=8.9, sale_price=8.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7b78ddfb-444a-4a7e-ae75-05de88dc84e7.jpg' WHERE id=104 AND product_id=74;

UPDATE products SET `product_name`='好丽友派', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/131c846a-48db-4d5b-83c0-5c9b4eaa8eec.jpg', `description`='好丽友派 · 巧克力派 / 6枚 / 204g', `detail_content`='<p>好丽友派</p><p>规格：巧克力派 / 6枚 / 204g</p>', `base_price`=9.9, version=version+1 WHERE id=75 AND product_code='PD000075';

UPDATE product_skus SET sku_name='好丽友派 / 巧克力派 / 6枚 / 204g', specification='{"规格": "巧克力派 / 6枚 / 204g"}', original_price=9.9, sale_price=9.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/131c846a-48db-4d5b-83c0-5c9b4eaa8eec.jpg' WHERE id=105 AND product_id=75;

UPDATE products SET `product_name`='盼盼小面包', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/6a1454b0-4a28-4dd5-8b32-b6ee6f306c21.jpg', `description`='盼盼小面包 · 小面包 / 400g', `detail_content`='<p>盼盼小面包</p><p>规格：小面包 / 400g</p>', `base_price`=19.9, version=version+1 WHERE id=76 AND product_code='PD000076';

UPDATE product_skus SET sku_name='盼盼小面包 / 小面包 / 400g', specification='{"规格": "小面包 / 400g"}', original_price=19.9, sale_price=19.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/6a1454b0-4a28-4dd5-8b32-b6ee6f306c21.jpg' WHERE id=106 AND product_id=76;

UPDATE products SET `product_name`='三只松鼠夏威夷果', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/6afd57df-3314-44a3-b24f-82ab88b33f8e.jpg', `description`='三只松鼠夏威夷果 · 奶油味夏威夷果 / 160g', `detail_content`='<p>三只松鼠夏威夷果</p><p>规格：奶油味夏威夷果 / 160g</p>', `base_price`=19.9, version=version+1 WHERE id=77 AND product_code='PD000077';

UPDATE product_skus SET sku_name='三只松鼠夏威夷果 / 奶油味夏威夷果 / 160g', specification='{"规格": "奶油味夏威夷果 / 160g"}', original_price=19.9, sale_price=19.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/6afd57df-3314-44a3-b24f-82ab88b33f8e.jpg' WHERE id=107 AND product_id=77;

UPDATE products SET `product_name`='立顿红茶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64cda48e-a8ca-470f-88c7-e05d624c75ae.jpg', `description`='立顿红茶 · 黄牌红茶 / 200g盒装', `detail_content`='<p>立顿红茶</p><p>规格：黄牌红茶 / 200g盒装</p>', `base_price`=29.9, version=version+1 WHERE id=78 AND product_code='PD000078';

UPDATE product_skus SET sku_name='立顿红茶 / 黄牌红茶 / 200g盒装', specification='{"规格": "黄牌红茶 / 200g盒装"}', original_price=29.9, sale_price=29.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64cda48e-a8ca-470f-88c7-e05d624c75ae.jpg' WHERE id=108 AND product_id=78;

UPDATE products SET `product_name`='雀巢速溶咖啡', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/480ddcfc-827e-43a5-9739-3dcc8581dbfb.jpg', `description`='雀巢速溶咖啡 · Clasico深烘速溶咖啡 / 300g', `detail_content`='<p>雀巢速溶咖啡</p><p>规格：Clasico深烘速溶咖啡 / 300g</p>', `base_price`=69.9, version=version+1 WHERE id=79 AND product_code='PD000079';

UPDATE product_skus SET sku_name='雀巢速溶咖啡 / Clasico深烘速溶咖啡 / 300g', specification='{"规格": "Clasico深烘速溶咖啡 / 300g"}', original_price=69.9, sale_price=69.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/480ddcfc-827e-43a5-9739-3dcc8581dbfb.jpg' WHERE id=109 AND product_id=79;

UPDATE products SET `product_name`='农夫山泉东方树叶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7e977330-9f26-434b-b91d-64dbdf9c8223.jpg', `description`='农夫山泉东方树叶 · 茉莉花茶 / 900mL', `detail_content`='<p>农夫山泉东方树叶</p><p>规格：茉莉花茶 / 900mL</p>', `base_price`=7.5, version=version+1 WHERE id=80 AND product_code='PD000080';

UPDATE product_skus SET sku_name='农夫山泉东方树叶 / 茉莉花茶 / 900mL', specification='{"规格": "茉莉花茶 / 900mL"}', original_price=7.5, sale_price=7.5, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7e977330-9f26-434b-b91d-64dbdf9c8223.jpg' WHERE id=110 AND product_id=80;

UPDATE products SET `product_name`='统一冰红茶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/50d5a836-6e9b-41b6-8ff6-8e8a76010cfb.jpg', `description`='统一冰红茶 · 柠檬味 / 1L单瓶', `detail_content`='<p>统一冰红茶</p><p>规格：柠檬味 / 1L单瓶</p>', `base_price`=4.5, version=version+1 WHERE id=81 AND product_code='PD000081';

UPDATE product_skus SET sku_name='统一冰红茶 / 柠檬味 / 1L单瓶', specification='{"规格": "柠檬味 / 1L单瓶"}', original_price=4.5, sale_price=4.5, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/50d5a836-6e9b-41b6-8ff6-8e8a76010cfb.jpg' WHERE id=111 AND product_id=81;

UPDATE products SET `product_name`='康师傅茉莉蜜茶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/fea929c4-2a7d-4789-8476-30d5bb39537f.jpg', `description`='康师傅茉莉蜜茶 · 茉莉蜜茶 / 500mL', `detail_content`='<p>康师傅茉莉蜜茶</p><p>规格：茉莉蜜茶 / 500mL</p>', `base_price`=3, version=version+1 WHERE id=82 AND product_code='PD000082';

UPDATE product_skus SET sku_name='康师傅茉莉蜜茶 / 茉莉蜜茶 / 500mL', specification='{"规格": "茉莉蜜茶 / 500mL"}', original_price=3, sale_price=3, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/fea929c4-2a7d-4789-8476-30d5bb39537f.jpg' WHERE id=112 AND product_id=82;

UPDATE products SET `product_name`='三得利乌龙茶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/d3437f61-d3d1-44e8-94c6-3c464c30824a.jpg', `description`='三得利乌龙茶 · 乌龙茶 / 500mL', `detail_content`='<p>三得利乌龙茶</p><p>规格：乌龙茶 / 500mL</p>', `base_price`=4.5, version=version+1 WHERE id=83 AND product_code='PD000083';

UPDATE product_skus SET sku_name='三得利乌龙茶 / 乌龙茶 / 500mL', specification='{"规格": "乌龙茶 / 500mL"}', original_price=4.5, sale_price=4.5, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/d3437f61-d3d1-44e8-94c6-3c464c30824a.jpg' WHERE id=113 AND product_id=83;

UPDATE products SET `product_name`='元气森林气泡水', `merchant_id`=4, `main_image`=NULL, `description`='元气森林气泡水 · 白桃味 / 480mL', `detail_content`='<p>元气森林气泡水</p><p>规格：白桃味 / 480mL</p>', `base_price`=5, version=version+1 WHERE id=84 AND product_code='PD000084';

UPDATE product_skus SET sku_name='元气森林气泡水 / 白桃味 / 480mL', specification='{"规格": "白桃味 / 480mL"}', original_price=5, sale_price=5, image_url=NULL WHERE id=114 AND product_id=84;

UPDATE products SET `product_name`='可口可乐', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/0b4cc1c3-7430-4a97-ae74-247a35f0809a.jpg', `description`='可口可乐 · 原味 / 500mL玻璃瓶', `detail_content`='<p>可口可乐</p><p>规格：原味 / 500mL玻璃瓶</p>', `base_price`=8.9, version=version+1 WHERE id=85 AND product_code='PD000085';

UPDATE product_skus SET sku_name='可口可乐 / 原味 / 500mL玻璃瓶', specification='{"规格": "原味 / 500mL玻璃瓶"}', original_price=8.9, sale_price=8.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/0b4cc1c3-7430-4a97-ae74-247a35f0809a.jpg' WHERE id=115 AND product_id=85;

UPDATE products SET `product_name`='百事可乐', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/090b8395-0499-4654-8d48-1765205582d2.jpg', `description`='百事可乐 · 生可乐 / 柠檬味 / 500mL', `detail_content`='<p>百事可乐</p><p>规格：生可乐 / 柠檬味 / 500mL</p>', `base_price`=4.5, version=version+1 WHERE id=86 AND product_code='PD000086';

UPDATE product_skus SET sku_name='百事可乐 / 生可乐 / 柠檬味 / 500mL', specification='{"规格": "生可乐 / 柠檬味 / 500mL"}', original_price=4.5, sale_price=4.5, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/090b8395-0499-4654-8d48-1765205582d2.jpg' WHERE id=116 AND product_id=86;

UPDATE products SET `product_name`='红牛功能饮料', `merchant_id`=4, `main_image`=NULL, `description`='红牛功能饮料 · 原味 / 250mL', `detail_content`='<p>红牛功能饮料</p><p>规格：原味 / 250mL</p>', `base_price`=6, version=version+1 WHERE id=87 AND product_code='PD000087';

UPDATE product_skus SET sku_name='红牛功能饮料 / 原味 / 250mL', specification='{"规格": "原味 / 250mL"}', original_price=6, sale_price=6, image_url=NULL WHERE id=117 AND product_id=87;

UPDATE products SET `product_name`='王老吉凉茶', `merchant_id`=4, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c29598ea-c3d3-4178-b662-19954063fe25.jpg', `description`='王老吉凉茶 · 凉茶 / 310mL×6罐', `detail_content`='<p>王老吉凉茶</p><p>规格：凉茶 / 310mL×6罐</p>', `base_price`=18.9, version=version+1 WHERE id=88 AND product_code='PD000088';

UPDATE product_skus SET sku_name='王老吉凉茶 / 凉茶 / 310mL×6罐', specification='{"规格": "凉茶 / 310mL×6罐"}', original_price=18.9, sale_price=18.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c29598ea-c3d3-4178-b662-19954063fe25.jpg' WHERE id=118 AND product_id=88;

UPDATE products SET `product_name`='全棉四件套', `merchant_id`=5, `main_image`=NULL, `description`='全棉四件套 · 1.8米床 / 四件套', `detail_content`='<p>全棉四件套</p><p>规格：1.8米床 / 四件套</p>', `base_price`=229, version=version+1 WHERE id=89 AND product_code='PD000089';

UPDATE product_skus SET sku_name='全棉四件套 / 1.8米床 / 四件套', specification='{"规格": "1.8米床 / 四件套"}', original_price=229, sale_price=229, image_url=NULL WHERE id=119 AND product_id=89;

UPDATE products SET `product_name`='记忆棉颈椎枕', `merchant_id`=5, `main_image`=NULL, `description`='记忆棉颈椎枕 · 单只 / 50x30cm', `detail_content`='<p>记忆棉颈椎枕</p><p>规格：单只 / 50x30cm</p>', `base_price`=99, version=version+1 WHERE id=90 AND product_code='PD000090';

UPDATE product_skus SET sku_name='记忆棉颈椎枕 / 单只 / 50x30cm', specification='{"规格": "单只 / 50x30cm"}', original_price=99, sale_price=99, image_url=NULL WHERE id=120 AND product_id=90;

UPDATE products SET `product_name`='蚕丝被', `merchant_id`=5, `main_image`=NULL, `description`='蚕丝被 · 200x230cm / 约2斤填充', `detail_content`='<p>蚕丝被</p><p>规格：200x230cm / 约2斤填充</p>', `base_price`=699, version=version+1 WHERE id=91 AND product_code='PD000091';

UPDATE product_skus SET sku_name='蚕丝被 / 200x230cm / 约2斤填充', specification='{"规格": "200x230cm / 约2斤填充"}', original_price=699, sale_price=699, image_url=NULL WHERE id=121 AND product_id=91;

UPDATE products SET `product_name`='羽绒被', `merchant_id`=5, `main_image`=NULL, `description`='羽绒被 · 200x230cm', `detail_content`='<p>羽绒被</p><p>规格：200x230cm</p>', `base_price`=599, version=version+1 WHERE id=92 AND product_code='PD000092';

UPDATE product_skus SET sku_name='羽绒被 / 200x230cm', specification='{"规格": "200x230cm"}', original_price=599, sale_price=599, image_url=NULL WHERE id=122 AND product_id=92;

UPDATE products SET `product_name`='乳胶床垫', `merchant_id`=5, `main_image`=NULL, `description`='乳胶床垫 · 150x200x5cm', `detail_content`='<p>乳胶床垫</p><p>规格：150x200x5cm</p>', `base_price`=999, version=version+1 WHERE id=93 AND product_code='PD000093';

UPDATE product_skus SET sku_name='乳胶床垫 / 150x200x5cm', specification='{"规格": "150x200x5cm"}', original_price=999, sale_price=999, image_url=NULL WHERE id=123 AND product_id=93;

UPDATE products SET `product_name`='纯棉床单', `merchant_id`=5, `main_image`=NULL, `description`='纯棉床单 · 230x250cm / 单条', `detail_content`='<p>纯棉床单</p><p>规格：230x250cm / 单条</p>', `base_price`=79, version=version+1 WHERE id=94 AND product_code='PD000094';

UPDATE product_skus SET sku_name='纯棉床单 / 230x250cm / 单条', specification='{"规格": "230x250cm / 单条"}', original_price=79, sale_price=79, image_url=NULL WHERE id=124 AND product_id=94;

UPDATE products SET `product_name`='磨毛四件套', `merchant_id`=5, `main_image`=NULL, `description`='磨毛四件套 · 1.8米床 / 四件套', `detail_content`='<p>磨毛四件套</p><p>规格：1.8米床 / 四件套</p>', `base_price`=269, version=version+1 WHERE id=95 AND product_code='PD000095';

UPDATE product_skus SET sku_name='磨毛四件套 / 1.8米床 / 四件套', specification='{"规格": "1.8米床 / 四件套"}', original_price=269, sale_price=269, image_url=NULL WHERE id=125 AND product_id=95;

UPDATE products SET `product_name`='天丝四件套', `merchant_id`=5, `main_image`=NULL, `description`='天丝四件套 · 1.8米床 / 四件套', `detail_content`='<p>天丝四件套</p><p>规格：1.8米床 / 四件套</p>', `base_price`=499, version=version+1 WHERE id=96 AND product_code='PD000096';

UPDATE product_skus SET sku_name='天丝四件套 / 1.8米床 / 四件套', specification='{"规格": "1.8米床 / 四件套"}', original_price=499, sale_price=499, image_url=NULL WHERE id=126 AND product_id=96;

UPDATE products SET `product_name`='真丝枕套', `merchant_id`=5, `main_image`=NULL, `description`='真丝枕套 · 48x74cm / 单只', `detail_content`='<p>真丝枕套</p><p>规格：48x74cm / 单只</p>', `base_price`=159, version=version+1 WHERE id=97 AND product_code='PD000097';

UPDATE product_skus SET sku_name='真丝枕套 / 48x74cm / 单只', specification='{"规格": "48x74cm / 单只"}', original_price=159, sale_price=159, image_url=NULL WHERE id=127 AND product_id=97;

UPDATE products SET `product_name`='夏凉被', `merchant_id`=5, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/5b61815b-a793-4bfe-97a5-f55c84d28710.jpg', `description`='夏凉被 · 203x229cm', `detail_content`='<p>夏凉被</p><p>规格：203x229cm</p>', `base_price`=99, version=version+1 WHERE id=98 AND product_code='PD000098';

UPDATE product_skus SET sku_name='夏凉被 / 203x229cm', specification='{"规格": "203x229cm"}', original_price=99, sale_price=99, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/5b61815b-a793-4bfe-97a5-f55c84d28710.jpg' WHERE id=128 AND product_id=98;

UPDATE products SET `product_name`='冬季加厚被子', `merchant_id`=5, `main_image`=NULL, `description`='冬季加厚被子 · 200x230cm', `detail_content`='<p>冬季加厚被子</p><p>规格：200x230cm</p>', `base_price`=199, version=version+1 WHERE id=99 AND product_code='PD000099';

UPDATE product_skus SET sku_name='冬季加厚被子 / 200x230cm', specification='{"规格": "200x230cm"}', original_price=199, sale_price=199, image_url=NULL WHERE id=129 AND product_id=99;

UPDATE products SET `product_name`='Nike Air Max', `merchant_id`=6, `main_image`=NULL, `description`='Nike Air Max · 42码', `detail_content`='<p>Nike Air Max</p><p>规格：42码</p>', `base_price`=699, version=version+1 WHERE id=100 AND product_code='PD000100';

UPDATE product_skus SET sku_name='Nike Air Max / 42码', specification='{"规格": "42码"}', original_price=699, sale_price=699, image_url=NULL WHERE id=130 AND product_id=100;

UPDATE products SET `product_name`='Adidas Ultraboost', `merchant_id`=6, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/048c0410-c9ff-434f-8107-675ce410e879.jpg', `description`='Adidas Ultraboost · 42码', `detail_content`='<p>Adidas Ultraboost</p><p>规格：42码</p>', `base_price`=799, version=version+1 WHERE id=101 AND product_code='PD000101';

UPDATE product_skus SET sku_name='Adidas Ultraboost / 42码', specification='{"规格": "42码"}', original_price=799, sale_price=799, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/048c0410-c9ff-434f-8107-675ce410e879.jpg' WHERE id=131 AND product_id=101;

UPDATE products SET `product_name`='New Balance 574', `merchant_id`=6, `main_image`=NULL, `description`='New Balance 574 · 42码', `detail_content`='<p>New Balance 574</p><p>规格：42码</p>', `base_price`=499, version=version+1 WHERE id=102 AND product_code='PD000102';

UPDATE product_skus SET sku_name='New Balance 574 / 42码', specification='{"规格": "42码"}', original_price=499, sale_price=499, image_url=NULL WHERE id=132 AND product_id=102;

UPDATE products SET `product_name`='Asics GEL-KAYANO', `merchant_id`=6, `main_image`=NULL, `description`='Asics GEL-KAYANO · 42码', `detail_content`='<p>Asics GEL-KAYANO</p><p>规格：42码</p>', `base_price`=999, version=version+1 WHERE id=103 AND product_code='PD000103';

UPDATE product_skus SET sku_name='Asics GEL-KAYANO / 42码', specification='{"规格": "42码"}', original_price=999, sale_price=999, image_url=NULL WHERE id=133 AND product_id=103;

UPDATE products SET `product_name`='Puma RS-X', `merchant_id`=6, `main_image`=NULL, `description`='Puma RS-X · 42码', `detail_content`='<p>Puma RS-X</p><p>规格：42码</p>', `base_price`=499, version=version+1 WHERE id=104 AND product_code='PD000104';

UPDATE product_skus SET sku_name='Puma RS-X / 42码', specification='{"规格": "42码"}', original_price=499, sale_price=499, image_url=NULL WHERE id=134 AND product_id=104;

UPDATE products SET `product_name`='李宁超轻系列', `merchant_id`=6, `main_image`=NULL, `description`='李宁超轻系列 · 42码', `detail_content`='<p>李宁超轻系列</p><p>规格：42码</p>', `base_price`=299, version=version+1 WHERE id=105 AND product_code='PD000105';

UPDATE product_skus SET sku_name='李宁超轻系列 / 42码', specification='{"规格": "42码"}', original_price=299, sale_price=299, image_url=NULL WHERE id=135 AND product_id=105;

UPDATE products SET `product_name`='安踏C37', `merchant_id`=6, `main_image`=NULL, `description`='安踏C37 · 42码', `detail_content`='<p>安踏C37</p><p>规格：42码</p>', `base_price`=229, version=version+1 WHERE id=106 AND product_code='PD000106';

UPDATE product_skus SET sku_name='安踏C37 / 42码', specification='{"规格": "42码"}', original_price=229, sale_price=229, image_url=NULL WHERE id=136 AND product_id=106;

UPDATE products SET `product_name`='特步竞速160X', `merchant_id`=6, `main_image`=NULL, `description`='特步竞速160X · 42码', `detail_content`='<p>特步竞速160X</p><p>规格：42码</p>', `base_price`=799, version=version+1 WHERE id=107 AND product_code='PD000107';

UPDATE product_skus SET sku_name='特步竞速160X / 42码', specification='{"规格": "42码"}', original_price=799, sale_price=799, image_url=NULL WHERE id=137 AND product_id=107;

UPDATE products SET `product_name`='乔丹飞影PB', `merchant_id`=6, `main_image`=NULL, `description`='乔丹飞影PB · 42码', `detail_content`='<p>乔丹飞影PB</p><p>规格：42码</p>', `base_price`=499, version=version+1 WHERE id=108 AND product_code='PD000108';

UPDATE product_skus SET sku_name='乔丹飞影PB / 42码', specification='{"规格": "42码"}', original_price=499, sale_price=499, image_url=NULL WHERE id=138 AND product_id=108;

UPDATE products SET `product_name`='鸿星尔克奇弹', `merchant_id`=6, `main_image`=NULL, `description`='鸿星尔克奇弹 · 42码', `detail_content`='<p>鸿星尔克奇弹</p><p>规格：42码</p>', `base_price`=199, version=version+1 WHERE id=109 AND product_code='PD000109';

UPDATE product_skus SET sku_name='鸿星尔克奇弹 / 42码', specification='{"规格": "42码"}', original_price=199, sale_price=199, image_url=NULL WHERE id=139 AND product_id=109;

UPDATE products SET `product_name`='Nike Pegasus', `merchant_id`=6, `main_image`=NULL, `description`='Nike Pegasus · 42码', `detail_content`='<p>Nike Pegasus</p><p>规格：42码</p>', `base_price`=599, version=version+1 WHERE id=110 AND product_code='PD000110';

UPDATE product_skus SET sku_name='Nike Pegasus / 42码', specification='{"规格": "42码"}', original_price=599, sale_price=599, image_url=NULL WHERE id=140 AND product_id=110;

UPDATE products SET `product_name`='兰蔻小黑瓶精华', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/aa40ac61-f128-4760-8aa5-d2fd1b3beb09.jpg', `description`='兰蔻小黑瓶精华 · 50mL', `detail_content`='<p>兰蔻小黑瓶精华</p><p>规格：50mL</p>', `base_price`=760, version=version+1 WHERE id=111 AND product_code='PD000111';

UPDATE product_skus SET sku_name='兰蔻小黑瓶精华 / 50mL', specification='{"规格": "50mL"}', original_price=760, sale_price=760, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/aa40ac61-f128-4760-8aa5-d2fd1b3beb09.jpg' WHERE id=141 AND product_id=111;

UPDATE products SET `product_name`='雅诗兰黛眼霜', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/ca314334-498d-436e-8294-978cf9cd03a4.jpg', `description`='雅诗兰黛眼霜 · 15mL', `detail_content`='<p>雅诗兰黛眼霜</p><p>规格：15mL</p>', `base_price`=520, version=version+1 WHERE id=112 AND product_code='PD000112';

UPDATE product_skus SET sku_name='雅诗兰黛眼霜 / 15mL', specification='{"规格": "15mL"}', original_price=520, sale_price=520, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/ca314334-498d-436e-8294-978cf9cd03a4.jpg' WHERE id=142 AND product_id=112;

UPDATE products SET `product_name`='SK-II神仙水', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/29f15837-cd3a-409b-aa81-3332c3907b95.jpg', `description`='SK-II神仙水 · 75mL', `detail_content`='<p>SK-II神仙水</p><p>规格：75mL</p>', `base_price`=399, version=version+1 WHERE id=113 AND product_code='PD000113';

UPDATE product_skus SET sku_name='SK-II神仙水 / 75mL', specification='{"规格": "75mL"}', original_price=399, sale_price=399, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/29f15837-cd3a-409b-aa81-3332c3907b95.jpg' WHERE id=143 AND product_id=113;

UPDATE products SET `product_name`='资生堂红腰子', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/685562ec-0b76-456c-a474-403eef7e43f3.jpg', `description`='资生堂红腰子 · 30mL', `detail_content`='<p>资生堂红腰子</p><p>规格：30mL</p>', `base_price`=399, version=version+1 WHERE id=114 AND product_code='PD000114';

UPDATE product_skus SET sku_name='资生堂红腰子 / 30mL', specification='{"规格": "30mL"}', original_price=399, sale_price=399, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/685562ec-0b76-456c-a474-403eef7e43f3.jpg' WHERE id=144 AND product_id=114;

UPDATE products SET `product_name`='海蓝之谜面霜', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/3c222a22-37d8-4bc3-b1ed-ddb98373dec1.jpg', `description`='海蓝之谜面霜 · 60mL', `detail_content`='<p>海蓝之谜面霜</p><p>规格：60mL</p>', `base_price`=2390, version=version+1 WHERE id=115 AND product_code='PD000115';

UPDATE product_skus SET sku_name='海蓝之谜面霜 / 60mL', specification='{"规格": "60mL"}', original_price=2390, sale_price=2390, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/3c222a22-37d8-4bc3-b1ed-ddb98373dec1.jpg' WHERE id=145 AND product_id=115;

UPDATE products SET `product_name`='科颜氏高保湿面霜', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/8dd095e7-b117-4faa-91cf-158e89e09656.jpg', `description`='科颜氏高保湿面霜 · 125mL', `detail_content`='<p>科颜氏高保湿面霜</p><p>规格：125mL</p>', `base_price`=499, version=version+1 WHERE id=116 AND product_code='PD000116';

UPDATE product_skus SET sku_name='科颜氏高保湿面霜 / 125mL', specification='{"规格": "125mL"}', original_price=499, sale_price=499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/8dd095e7-b117-4faa-91cf-158e89e09656.jpg' WHERE id=146 AND product_id=116;

UPDATE products SET `product_name`='悦木之源菌菇水', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/f264ec71-a874-4574-8677-618362157304.jpg', `description`='悦木之源菌菇水 · 200mL', `detail_content`='<p>悦木之源菌菇水</p><p>规格：200mL</p>', `base_price`=320, version=version+1 WHERE id=117 AND product_code='PD000117';

UPDATE product_skus SET sku_name='悦木之源菌菇水 / 200mL', specification='{"规格": "200mL"}', original_price=320, sale_price=320, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/f264ec71-a874-4574-8677-618362157304.jpg' WHERE id=147 AND product_id=117;

UPDATE products SET `product_name`='薇姿温泉喷雾', `merchant_id`=7, `main_image`=NULL, `description`='薇姿温泉喷雾 · 150mL', `detail_content`='<p>薇姿温泉喷雾</p><p>规格：150mL</p>', `base_price`=109, version=version+1 WHERE id=118 AND product_code='PD000118';

UPDATE product_skus SET sku_name='薇姿温泉喷雾 / 150mL', specification='{"规格": "150mL"}', original_price=109, sale_price=109, image_url=NULL WHERE id=148 AND product_id=118;

UPDATE products SET `product_name`='理肤泉B5面膜', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/2b14e27e-d5f5-45a0-827f-aaccacdb90da.jpg', `description`='理肤泉B5面膜 · 5片装', `detail_content`='<p>理肤泉B5面膜</p><p>规格：5片装</p>', `base_price`=139, version=version+1 WHERE id=119 AND product_code='PD000119';

UPDATE product_skus SET sku_name='理肤泉B5面膜 / 5片装', specification='{"规格": "5片装"}', original_price=139, sale_price=139, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/2b14e27e-d5f5-45a0-827f-aaccacdb90da.jpg' WHERE id=149 AND product_id=119;

UPDATE products SET `product_name`='修丽可CE精华', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/8cee06da-1163-499b-bf01-da4f023ace44.jpg', `description`='修丽可CE精华 · 30mL', `detail_content`='<p>修丽可CE精华</p><p>规格：30mL</p>', `base_price`=1290, version=version+1 WHERE id=120 AND product_code='PD000120';

UPDATE product_skus SET sku_name='修丽可CE精华 / 30mL', specification='{"规格": "30mL"}', original_price=1290, sale_price=1290, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/8cee06da-1163-499b-bf01-da4f023ace44.jpg' WHERE id=150 AND product_id=120;

UPDATE products SET `product_name`='珀莱雅双抗精华', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/34f12429-9a06-4c05-b040-8236ecfd6f56.jpg', `description`='珀莱雅双抗精华 · 30mL', `detail_content`='<p>珀莱雅双抗精华</p><p>规格：30mL</p>', `base_price`=299, version=version+1 WHERE id=121 AND product_code='PD000121';

UPDATE product_skus SET sku_name='珀莱雅双抗精华 / 30mL', specification='{"规格": "30mL"}', original_price=299, sale_price=299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/34f12429-9a06-4c05-b040-8236ecfd6f56.jpg' WHERE id=151 AND product_id=121;

UPDATE products SET `product_name`='MAC子弹头口红', `merchant_id`=7, `main_image`=NULL, `description`='MAC子弹头口红 · 3g / 正红色', `detail_content`='<p>MAC子弹头口红</p><p>规格：3g / 正红色</p>', `base_price`=199, version=version+1 WHERE id=122 AND product_code='PD000122';

UPDATE product_skus SET sku_name='MAC子弹头口红 / 3g / 正红色', specification='{"规格": "3g / 正红色"}', original_price=199, sale_price=199, image_url=NULL WHERE id=152 AND product_id=122;

UPDATE products SET `product_name`='YSL圣罗兰粉底', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/321517e8-c63b-4c2c-bc1e-a07e68dff61d.jpg', `description`='YSL圣罗兰粉底 · 恒颜焕亮 / LC1 / 25mL', `detail_content`='<p>YSL圣罗兰粉底</p><p>规格：恒颜焕亮 / LC1 / 25mL</p>', `base_price`=490, version=version+1 WHERE id=123 AND product_code='PD000123';

UPDATE product_skus SET sku_name='YSL圣罗兰粉底 / 恒颜焕亮 / LC1 / 25mL', specification='{"规格": "恒颜焕亮 / LC1 / 25mL"}', original_price=490, sale_price=490, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/321517e8-c63b-4c2c-bc1e-a07e68dff61d.jpg' WHERE id=153 AND product_id=123;

UPDATE products SET `product_name`='迪奥烈艳蓝金', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/2eeb50ec-2f2b-42e7-ab1d-838eca0352c7.jpg', `description`='迪奥烈艳蓝金 · 3.5g / 772豆沙红', `detail_content`='<p>迪奥烈艳蓝金</p><p>规格：3.5g / 772豆沙红</p>', `base_price`=350, version=version+1 WHERE id=124 AND product_code='PD000124';

UPDATE product_skus SET sku_name='迪奥烈艳蓝金 / 3.5g / 772豆沙红', specification='{"规格": "3.5g / 772豆沙红"}', original_price=350, sale_price=350, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/2eeb50ec-2f2b-42e7-ab1d-838eca0352c7.jpg' WHERE id=154 AND product_id=124;

UPDATE products SET `product_name`='香奈儿气垫', `merchant_id`=7, `main_image`=NULL, `description`='香奈儿气垫 · 15g', `detail_content`='<p>香奈儿气垫</p><p>规格：15g</p>', `base_price`=610, version=version+1 WHERE id=125 AND product_code='PD000125';

UPDATE product_skus SET sku_name='香奈儿气垫 / 15g', specification='{"规格": "15g"}', original_price=610, sale_price=610, image_url=NULL WHERE id=155 AND product_id=125;

UPDATE products SET `product_name`='阿玛尼权力粉底', `merchant_id`=7, `main_image`=NULL, `description`='阿玛尼权力粉底 · 30mL', `detail_content`='<p>阿玛尼权力粉底</p><p>规格：30mL</p>', `base_price`=499, version=version+1 WHERE id=126 AND product_code='PD000126';

UPDATE product_skus SET sku_name='阿玛尼权力粉底 / 30mL', specification='{"规格": "30mL"}', original_price=499, sale_price=499, image_url=NULL WHERE id=156 AND product_id=126;

UPDATE products SET `product_name`='NARS腮红', `merchant_id`=7, `main_image`=NULL, `description`='NARS腮红 · 4.8g', `detail_content`='<p>NARS腮红</p><p>规格：4.8g</p>', `base_price`=260, version=version+1 WHERE id=127 AND product_code='PD000127';

UPDATE product_skus SET sku_name='NARS腮红 / 4.8g', specification='{"规格": "4.8g"}', original_price=260, sale_price=260, image_url=NULL WHERE id=157 AND product_id=127;

UPDATE products SET `product_name`='植村秀砍刀眉笔', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/42b0f6c7-ca38-459d-b0c4-50c73e015290.jpg', `description`='植村秀砍刀眉笔 · 4g / 01黑色', `detail_content`='<p>植村秀砍刀眉笔</p><p>规格：4g / 01黑色</p>', `base_price`=199, version=version+1 WHERE id=128 AND product_code='PD000128';

UPDATE product_skus SET sku_name='植村秀砍刀眉笔 / 4g / 01黑色', specification='{"规格": "4g / 01黑色"}', original_price=199, sale_price=199, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/42b0f6c7-ca38-459d-b0c4-50c73e015290.jpg' WHERE id=158 AND product_id=128;

UPDATE products SET `product_name`='美宝莲FIT ME', `merchant_id`=7, `main_image`=NULL, `description`='美宝莲FIT ME · 30mL', `detail_content`='<p>美宝莲FIT ME</p><p>规格：30mL</p>', `base_price`=119, version=version+1 WHERE id=129 AND product_code='PD000129';

UPDATE product_skus SET sku_name='美宝莲FIT ME / 30mL', specification='{"规格": "30mL"}', original_price=119, sale_price=119, image_url=NULL WHERE id=159 AND product_id=129;

UPDATE products SET `product_name`='卡姿兰大眼睛睫毛膏', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd0c3414-5392-436f-a841-3a80501ad7eb.jpg', `description`='卡姿兰大眼睛睫毛膏 · 纤长款 / 黑色 / 1支', `detail_content`='<p>卡姿兰大眼睛睫毛膏</p><p>规格：纤长款 / 黑色 / 1支</p>', `base_price`=89, version=version+1 WHERE id=130 AND product_code='PD000130';

UPDATE product_skus SET sku_name='卡姿兰大眼睛睫毛膏 / 纤长款 / 黑色 / 1支', specification='{"规格": "纤长款 / 黑色 / 1支"}', original_price=89, sale_price=89, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd0c3414-5392-436f-a841-3a80501ad7eb.jpg' WHERE id=160 AND product_id=130;

UPDATE products SET `product_name`='完美日记动物盘', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/62df34d4-5f1e-4bc1-95bc-d331f6dcc717.jpg', `description`='完美日记动物盘 · 熊猫盘 / 12色', `detail_content`='<p>完美日记动物盘</p><p>规格：熊猫盘 / 12色</p>', `base_price`=169, version=version+1 WHERE id=131 AND product_code='PD000131';

UPDATE product_skus SET sku_name='完美日记动物盘 / 熊猫盘 / 12色', specification='{"规格": "熊猫盘 / 12色"}', original_price=169, sale_price=169, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/62df34d4-5f1e-4bc1-95bc-d331f6dcc717.jpg' WHERE id=161 AND product_id=131;

UPDATE products SET `product_name`='花西子蜜粉', `merchant_id`=7, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/0ffb7d66-c7fd-4977-8f7d-d17ef6a9b613.jpg', `description`='花西子蜜粉 · 紫色 / 8.5g', `detail_content`='<p>花西子蜜粉</p><p>规格：紫色 / 8.5g</p>', `base_price`=149, version=version+1 WHERE id=132 AND product_code='PD000132';

UPDATE product_skus SET sku_name='花西子蜜粉 / 紫色 / 8.5g', specification='{"规格": "紫色 / 8.5g"}', original_price=149, sale_price=149, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/0ffb7d66-c7fd-4977-8f7d-d17ef6a9b613.jpg' WHERE id=162 AND product_id=132;

UPDATE products SET `product_name`='iPhone 15 Pro Max', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7bb99fe9-9fdf-46c8-be53-0c5e97a54038.jpg', `description`='iPhone 15 Pro Max · 256GB', `detail_content`='<p>iPhone 15 Pro Max</p><p>规格：256GB</p>', `base_price`=5999, version=version+1 WHERE id=133 AND product_code='PD000133';

UPDATE product_skus SET sku_name='iPhone 15 Pro Max / 256GB', specification='{"规格": "256GB"}', original_price=5999, sale_price=5999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/7bb99fe9-9fdf-46c8-be53-0c5e97a54038.jpg' WHERE id=163 AND product_id=133;

UPDATE products SET `product_name`='华为Mate 60 Pro', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c828c855-861b-4b9e-8182-8541d4bec262.jpg', `description`='华为Mate 60 Pro · 12GB+256GB', `detail_content`='<p>华为Mate 60 Pro</p><p>规格：12GB+256GB</p>', `base_price`=4599, version=version+1 WHERE id=134 AND product_code='PD000134';

UPDATE product_skus SET sku_name='华为Mate 60 Pro / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=4599, sale_price=4599, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c828c855-861b-4b9e-8182-8541d4bec262.jpg' WHERE id=164 AND product_id=134;

UPDATE products SET `product_name`='小米14 Ultra', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/1190d8ec-8bef-4ff9-9959-a820be8d5b31.jpg', `description`='小米14 Ultra · 12GB+256GB', `detail_content`='<p>小米14 Ultra</p><p>规格：12GB+256GB</p>', `base_price`=5499, version=version+1 WHERE id=135 AND product_code='PD000135';

UPDATE product_skus SET sku_name='小米14 Ultra / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=5499, sale_price=5499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/1190d8ec-8bef-4ff9-9959-a820be8d5b31.jpg' WHERE id=165 AND product_id=135;

UPDATE products SET `product_name`='OPPO Find X7', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/07c88d3c-6be8-480e-abc6-949b419ff883.jpg', `description`='OPPO Find X7 · 12GB+256GB', `detail_content`='<p>OPPO Find X7</p><p>规格：12GB+256GB</p>', `base_price`=2999, version=version+1 WHERE id=136 AND product_code='PD000136';

UPDATE product_skus SET sku_name='OPPO Find X7 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2999, sale_price=2999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/07c88d3c-6be8-480e-abc6-949b419ff883.jpg' WHERE id=166 AND product_id=136;

UPDATE products SET `product_name`='vivo X100 Pro', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64e1cf2b-7833-40c4-90c7-be2cca3e9760.jpg', `description`='vivo X100 Pro · 12GB+256GB', `detail_content`='<p>vivo X100 Pro</p><p>规格：12GB+256GB</p>', `base_price`=3999, version=version+1 WHERE id=137 AND product_code='PD000137';

UPDATE product_skus SET sku_name='vivo X100 Pro / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3999, sale_price=3999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/64e1cf2b-7833-40c4-90c7-be2cca3e9760.jpg' WHERE id=167 AND product_id=137;

UPDATE products SET `product_name`='三星Galaxy S24', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd52b49f-94b0-4e34-a81c-292dc66b3588.jpg', `description`='三星Galaxy S24 · 8GB+256GB', `detail_content`='<p>三星Galaxy S24</p><p>规格：8GB+256GB</p>', `base_price`=3499, version=version+1 WHERE id=138 AND product_code='PD000138';

UPDATE product_skus SET sku_name='三星Galaxy S24 / 8GB+256GB', specification='{"规格": "8GB+256GB"}', original_price=3499, sale_price=3499, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cd52b49f-94b0-4e34-a81c-292dc66b3588.jpg' WHERE id=168 AND product_id=138;

UPDATE products SET `product_name`='荣耀Magic6', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e508d2ae-889f-4b09-ac0f-9c3a672280ad.jpg', `description`='荣耀Magic6 · 12GB+256GB', `detail_content`='<p>荣耀Magic6</p><p>规格：12GB+256GB</p>', `base_price`=3299, version=version+1 WHERE id=139 AND product_code='PD000139';

UPDATE product_skus SET sku_name='荣耀Magic6 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3299, sale_price=3299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e508d2ae-889f-4b09-ac0f-9c3a672280ad.jpg' WHERE id=169 AND product_id=139;

UPDATE products SET `product_name`='一加12', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/489b9d84-78ac-43b4-8dc5-78ff923ff710.jpg', `description`='一加12 · 12GB+256GB', `detail_content`='<p>一加12</p><p>规格：12GB+256GB</p>', `base_price`=2999, version=version+1 WHERE id=140 AND product_code='PD000140';

UPDATE product_skus SET sku_name='一加12 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2999, sale_price=2999, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/489b9d84-78ac-43b4-8dc5-78ff923ff710.jpg' WHERE id=170 AND product_id=140;

UPDATE products SET `product_name`='魅族21', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/11c5a01f-c605-4d44-98bc-21f0055d13f9.jpg', `description`='魅族21 · 12GB+256GB', `detail_content`='<p>魅族21</p><p>规格：12GB+256GB</p>', `base_price`=2299, version=version+1 WHERE id=141 AND product_code='PD000141';

UPDATE product_skus SET sku_name='魅族21 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2299, sale_price=2299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/11c5a01f-c605-4d44-98bc-21f0055d13f9.jpg' WHERE id=171 AND product_id=141;

UPDATE products SET `product_name`='努比亚Z60 Ultra', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/4b1b033c-6f96-4eaf-a78f-6dda8afdf7b8.jpg', `description`='努比亚Z60 Ultra · 12GB+256GB', `detail_content`='<p>努比亚Z60 Ultra</p><p>规格：12GB+256GB</p>', `base_price`=3299, version=version+1 WHERE id=142 AND product_code='PD000142';

UPDATE product_skus SET sku_name='努比亚Z60 Ultra / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=3299, sale_price=3299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/4b1b033c-6f96-4eaf-a78f-6dda8afdf7b8.jpg' WHERE id=172 AND product_id=142;

UPDATE products SET `product_name`='realme GT5', `merchant_id`=10, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4f66f93-af62-485c-b841-73caf67b8903.jpg', `description`='realme GT5 · 12GB+256GB', `detail_content`='<p>realme GT5</p><p>规格：12GB+256GB</p>', `base_price`=2299, version=version+1 WHERE id=143 AND product_code='PD000143';

UPDATE product_skus SET sku_name='realme GT5 / 12GB+256GB', specification='{"规格": "12GB+256GB"}', original_price=2299, sale_price=2299, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4f66f93-af62-485c-b841-73caf67b8903.jpg' WHERE id=173 AND product_id=143;

UPDATE products SET `product_name`='三只松鼠坚果大礼包', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/41e589b2-2f26-4564-aa4f-a3c1f78913e0.jpg', `description`='三只松鼠坚果大礼包 · 混合坚果大礼包 / 650g', `detail_content`='<p>三只松鼠坚果大礼包</p><p>规格：混合坚果大礼包 / 650g</p>', `base_price`=59.9, version=version+1 WHERE id=144 AND product_code='PD000144';

UPDATE product_skus SET sku_name='三只松鼠坚果大礼包 / 混合坚果大礼包 / 650g', specification='{"规格": "混合坚果大礼包 / 650g"}', original_price=59.9, sale_price=59.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/41e589b2-2f26-4564-aa4f-a3c1f78913e0.jpg' WHERE id=174 AND product_id=144;

UPDATE products SET `product_name`='良品铺子芒果干', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a1cd5d1f-d88b-49bc-b274-1656bdb9e6f2.jpg', `description`='良品铺子芒果干 · 芒果干 / 108g', `detail_content`='<p>良品铺子芒果干</p><p>规格：芒果干 / 108g</p>', `base_price`=19.9, version=version+1 WHERE id=145 AND product_code='PD000145';

UPDATE product_skus SET sku_name='良品铺子芒果干 / 芒果干 / 108g', specification='{"规格": "芒果干 / 108g"}', original_price=19.9, sale_price=19.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/a1cd5d1f-d88b-49bc-b274-1656bdb9e6f2.jpg' WHERE id=175 AND product_id=145;

UPDATE products SET `product_name`='百草味手撕面包', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e7d033f4-08d7-4d2e-8517-47c4032b8de9.jpg', `description`='百草味手撕面包 · 手撕面包 / 280g', `detail_content`='<p>百草味手撕面包</p><p>规格：手撕面包 / 280g</p>', `base_price`=12.9, version=version+1 WHERE id=146 AND product_code='PD000146';

UPDATE product_skus SET sku_name='百草味手撕面包 / 手撕面包 / 280g', specification='{"规格": "手撕面包 / 280g"}', original_price=12.9, sale_price=12.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/e7d033f4-08d7-4d2e-8517-47c4032b8de9.jpg' WHERE id=176 AND product_id=146;

UPDATE products SET `product_name`='洽洽每日坚果', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cee72b85-9903-4bd3-acfe-9becd7e0c170.jpg', `description`='洽洽每日坚果 · 小黄袋 / 15g×30袋 / 450g', `detail_content`='<p>洽洽每日坚果</p><p>规格：小黄袋 / 15g×30袋 / 450g</p>', `base_price`=39.9, version=version+1 WHERE id=147 AND product_code='PD000147';

UPDATE product_skus SET sku_name='洽洽每日坚果 / 小黄袋 / 15g×30袋 / 450g', specification='{"规格": "小黄袋 / 15g×30袋 / 450g"}', original_price=39.9, sale_price=39.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/cee72b85-9903-4bd3-acfe-9becd7e0c170.jpg' WHERE id=177 AND product_id=147;

UPDATE products SET `product_name`='乐事薯片', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/52059ec7-d91a-4f16-a84e-206ab9a08dd1.jpg', `description`='乐事薯片 · 意大利香浓红烩味 / 70g', `detail_content`='<p>乐事薯片</p><p>规格：意大利香浓红烩味 / 70g</p>', `base_price`=7.9, version=version+1 WHERE id=148 AND product_code='PD000148';

UPDATE product_skus SET sku_name='乐事薯片 / 意大利香浓红烩味 / 70g', specification='{"规格": "意大利香浓红烩味 / 70g"}', original_price=7.9, sale_price=7.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/52059ec7-d91a-4f16-a84e-206ab9a08dd1.jpg' WHERE id=178 AND product_id=148;

UPDATE products SET `product_name`='奥利奥饼干', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/08d6fbae-bce3-4362-a20c-91c8d3eaa277.jpg', `description`='奥利奥饼干 · 草莓奶油味 / 100g', `detail_content`='<p>奥利奥饼干</p><p>规格：草莓奶油味 / 100g</p>', `base_price`=9.9, version=version+1 WHERE id=149 AND product_code='PD000149';

UPDATE product_skus SET sku_name='奥利奥饼干 / 草莓奶油味 / 100g', specification='{"规格": "草莓奶油味 / 100g"}', original_price=9.9, sale_price=9.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/08d6fbae-bce3-4362-a20c-91c8d3eaa277.jpg' WHERE id=179 AND product_id=149;

UPDATE products SET `product_name`='旺旺雪饼', `merchant_id`=14, `main_image`='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4e719da-54d0-4e25-9daf-3c6b99ac68ef.jpg', `description`='旺旺雪饼 · 雪饼 / 520g', `detail_content`='<p>旺旺雪饼</p><p>规格：雪饼 / 520g</p>', `base_price`=16.9, version=version+1 WHERE id=150 AND product_code='PD000150';

UPDATE product_skus SET sku_name='旺旺雪饼 / 雪饼 / 520g', specification='{"规格": "雪饼 / 520g"}', original_price=16.9, sale_price=16.9, image_url='https://miaomiao-max.oss-cn-beijing.aliyuncs.com/c4e719da-54d0-4e25-9daf-3c6b99ac68ef.jpg' WHERE id=180 AND product_id=150;

UPDATE categories SET category_name='数码配件' WHERE id=11 AND category_name='手机配件';

UPDATE categories SET category_name='饮料与茶饮' WHERE id=16 AND category_name='茶饮冲调';

CREATE TEMPORARY TABLE catalog_seed_items AS
SELECT oi.id, oi.order_id FROM order_items oi JOIN orders o ON o.id=oi.order_id
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$'
AND oi.product_name REGEXP '^商品[0-9]+$' AND oi.sku_name REGEXP '^SKU[0-9]+$';

UPDATE order_items oi JOIN catalog_seed_items seed ON seed.id=oi.id
JOIN products p ON p.id=oi.product_id JOIN product_skus s ON s.id=oi.sku_id
SET oi.product_name=p.product_name, oi.sku_name=s.sku_name,
oi.sku_specification=s.specification, oi.product_image=COALESCE(s.image_url,p.main_image),
oi.unit_price=s.sale_price, oi.total_amount=ROUND(s.sale_price*oi.quantity,2);

UPDATE orders o JOIN
(SELECT order_id,SUM(total_amount) amount FROM order_items GROUP BY order_id) totals ON totals.order_id=o.id
SET o.total_amount=totals.amount, o.payable_amount=totals.amount+o.freight_amount, o.version=o.version+1
WHERE o.total_amount<>totals.amount OR o.payable_amount<>totals.amount+o.freight_amount;

UPDATE orders SET order_status='待发货', version=version+1
WHERE order_status='已支付' AND payment_status='已支付';

UPDATE orders SET payment_status='未支付', delivery_status='待发货',
paid_at=NULL, shipped_at=NULL, completed_at=NULL,
cancelled_at=GREATEST(COALESCE(cancelled_at,DATE_ADD(created_at,INTERVAL 1 HOUR)),created_at),
cancel_reason=COALESCE(NULLIF(cancel_reason,''),'用户取消演示订单'), version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status='已取消';

UPDATE orders SET paid_at=GREATEST(COALESCE(paid_at,DATE_ADD(created_at,INTERVAL 30 MINUTE)),created_at),
payment_status='已支付', version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status IN ('待发货','已发货','已完成');

UPDATE orders SET shipped_at=GREATEST(COALESCE(shipped_at,DATE_ADD(paid_at,INTERVAL 1 DAY)),paid_at),
delivery_status=IF(order_status='已完成','已签收','已发货'), version=version+1
WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status IN ('已发货','已完成');

UPDATE orders SET completed_at=GREATEST(COALESCE(completed_at,DATE_ADD(shipped_at,INTERVAL 2 DAY)),shipped_at),
version=version+1 WHERE id BETWEEN 1 AND 100 AND order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND order_status='已完成';

UPDATE order_items oi JOIN orders o ON o.id=oi.order_id
SET oi.shipped_at=o.shipped_at
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.order_status IN ('已发货','已完成')
AND (oi.shipped_at IS NULL OR oi.shipped_at<o.paid_at);

UPDATE order_items oi JOIN orders o ON o.id=oi.order_id SET oi.shipped_at=NULL
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.order_status IN ('已取消','待付款');

UPDATE inventories inv LEFT JOIN (
SELECT oi.sku_id,SUM(oi.quantity) qty FROM order_items oi JOIN orders o ON o.id=oi.order_id
WHERE o.order_status IN ('待付款','待发货') AND oi.shipped_at IS NULL GROUP BY oi.sku_id
) pending ON pending.sku_id=inv.sku_id
SET inv.locked_quantity=COALESCE(pending.qty,0), inv.version=inv.version+1
WHERE inv.locked_quantity<>COALESCE(pending.qty,0);

UPDATE user_addresses a JOIN users u ON u.id=a.user_id SET a.receiver_name=COALESCE(NULLIF(u.real_name,''),u.nickname)
WHERE a.receiver_name REGEXP '^user[0-9]+([(].*[)])?$' AND u.real_name IS NOT NULL;

UPDATE orders o JOIN users u ON u.id=o.user_id
SET o.receiver_name=COALESCE(NULLIF(u.real_name,''),u.nickname),o.version=o.version+1
WHERE o.id BETWEEN 1 AND 100 AND o.order_no REGEXP '^ORD2025(0[1-9]|1[0-2])[0-9]{3}$' AND o.receiver_name REGEXP '^user[0-9]+';

DROP TEMPORARY TABLE catalog_seed_items;

COMMIT;
