-- orders
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2000, 10000, NOW(), 1, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1500, 12000, NOW(), 2, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1000, 11000, NOW(), 3, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2500, 13000, NOW(), 4, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 3000, 14000, NOW(), 5, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 3500, 9000, NOW(), 6, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1000, 10500, NOW(), 7, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2000, 11500, NOW(), 8, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1800, 9800, NOW(), 9, 'PAID');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2300, 10200, NOW(), 10, 'PAID');

-- order_item
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (5, 1, 1, 10000, 10000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (2, 2, 2, 12000, 12000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (3, 3, 3, 9000, 9000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 4, 4, 11000, 11000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (4, 5, 5, 15000, 15000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (3, 6, 6, 13000, 13000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (2, 7, 7, 8000, 8000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 8, 8, 14000, 14000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 9, 9, 12500, 12500);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (3, 10, 10, 9500, 9500);