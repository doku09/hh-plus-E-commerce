CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price BIGINT NOT NULL,
    discount_price BIGINT,
    status VARCHAR(255),
    user_id BIGINT,
    coupon_id BIGINT
);

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    CONSTRAINT fk_order_item_order
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
    ON DELETE CASCADE
);