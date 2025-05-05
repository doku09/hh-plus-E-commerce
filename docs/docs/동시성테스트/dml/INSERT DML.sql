-- users
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user1');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user2');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user3');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user4');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user5');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user6');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user7');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user8');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user9');
INSERT INTO users (created_at, updated_at, username) VALUES (NOW(), NOW(), 'user10');

-- coupon
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (0, 100, 3000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰1');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (1, 100, 2000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰2');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (1, 100, 2500, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰3');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (0, 100, 4000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰4');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (1, 100, 1000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰5');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (0, 100, 5000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰6');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (0, 100, 3500, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰7');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (1, 100, 1500, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰8');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (0, 100, 3000, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰9');
INSERT INTO coupon (coupon_type, quantity, discount_price, expired_date, use_start_date, name) VALUES (1, 100, 2500, NOW() + INTERVAL 30 DAY, NOW(), '쿠폰10');

-- product
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 10000, NOW(), '상품1');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 12000, NOW(), '상품2');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 9000, NOW(), '상품3');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 11000, NOW(), '상품4');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 15000, NOW(), '상품5');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 13000, NOW(), '상품6');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 8000, NOW(), '상품7');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 14000, NOW(), '상품8');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 12500, NOW(), '상품9');
INSERT INTO product (created_at, price, updated_at, name) VALUES (NOW(), 9500, NOW(), '상품10');

-- product_stock
INSERT INTO product_stock (quantity, product_id, version) VALUES (5, 1, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (6, 2, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (4, 3, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (8, 4, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (3, 5, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (10, 6, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (7, 7, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (9, 8, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (2, 9, 0);
INSERT INTO product_stock (quantity, product_id, version) VALUES (1, 10, 0);

-- point
INSERT INTO point (amount, user_id, version) VALUES (30000, 1, 0);
INSERT INTO point (amount, user_id, version) VALUES (35000, 2, 0);
INSERT INTO point (amount, user_id, version) VALUES (27000, 3, 0);
INSERT INTO point (amount, user_id, version) VALUES (29000, 4, 0);
INSERT INTO point (amount, user_id, version) VALUES (41000, 5, 0);
INSERT INTO point (amount, user_id, version) VALUES (33000, 6, 0);
INSERT INTO point (amount, user_id, version) VALUES (37000, 7, 0);
INSERT INTO point (amount, user_id, version) VALUES (22000, 8, 0);
INSERT INTO point (amount, user_id, version) VALUES (45000, 9, 0);
INSERT INTO point (amount, user_id, version) VALUES (39000, 10, 0);

-- orders
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2000, 10000, NOW(), 1, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1500, 12000, NOW(), 2, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1000, 11000, NOW(), 3, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2500, 13000, NOW(), 4, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 3000, 14000, NOW(), 5, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 3500, 9000, NOW(), 6, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1000, 10500, NOW(), 7, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2000, 11500, NOW(), 8, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 1800, 9800, NOW(), 9, 'ORDERED');
INSERT INTO orders (coupon_id, created_at, discount_price, total_price, updated_at, user_id, status) VALUES (NULL, NOW(), 2300, 10200, NOW(), 10, 'ORDERED');

-- order_item
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 1, 1, 10000, 10000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 2, 2, 12000, 12000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 3, 3, 9000, 9000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 4, 4, 11000, 11000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 5, 5, 15000, 15000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 6, 6, 13000, 13000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 7, 7, 8000, 8000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 8, 8, 14000, 14000);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 9, 9, 12500, 12500);
INSERT INTO order_item (quantity, order_id, product_id, product_price, total_price) VALUES (1, 10, 10, 9500, 9500);

-- payment
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (1, NOW(), 1, 10000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (2, NOW(), 2, 12000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (0, NOW(), 3, 11000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (1, NOW(), 4, 13000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (2, NOW(), 5, 14000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (0, NOW(), 6, 9000, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (1, NOW(), 7, 10500, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (2, NOW(), 8, 11500, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (1, NOW(), 9, 9800, NOW());
INSERT INTO payment (status, created_at, order_id, pay_price, updated_at) VALUES (0, NOW(), 10, 10200, NOW());

-- user_coupon
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (1, 1, NOW(), NOW() - INTERVAL 5 DAY, 1);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (0, 2, NOW(), NOW() - INTERVAL 5 DAY, 2);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (1, 3, NOW(), NOW() - INTERVAL 5 DAY, 3);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (0, 4, NOW(), NOW() - INTERVAL 5 DAY, 4);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (1, 5, NOW(), NOW() - INTERVAL 5 DAY, 5);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (0, 6, NOW(), NOW() - INTERVAL 5 DAY, 6);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (1, 7, NOW(), NOW() - INTERVAL 5 DAY, 7);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (0, 8, NOW(), NOW() - INTERVAL 5 DAY, 8);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (1, 9, NOW(), NOW() - INTERVAL 5 DAY, 9);
INSERT INTO user_coupon (status, coupon_id, issued_at, used_at, user_id) VALUES (0, 10, NOW(), NOW() - INTERVAL 5 DAY, 10);