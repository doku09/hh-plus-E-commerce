-- 1. users 테이블
SELECT * FROM users;

-- 2. coupon 테이블
SELECT * FROM coupon;

-- 3. product 테이블
SELECT * FROM product;

-- 4. product_stock 테이블
SELECT * FROM product_stock;

-- 5. point 테이블
SELECT * FROM point;

-- 6. orders 테이블
SELECT * FROM orders;

-- 7. order_item 테이블
SELECT * FROM order_item;

-- 8. payment 테이블
SELECT * FROM payment;

-- 9. user_coupon 테이블
SELECT * FROM user_coupon;

-- 10. (외래키 포함 여부 확인용 조인 예시) 주문과 주문상품 조인
SELECT o.order_id, o.user_id, oi.product_id, oi.total_price
FROM orders o
         JOIN order_item oi ON o.order_id = oi.order_id;

-- 필요시: product_stock과 product 조인
SELECT ps.id AS stock_id, ps.quantity, p.name AS product_name
FROM product_stock ps
         JOIN product p ON ps.product_id = p.product_id;