
INSERT INTO product (product_id, name, price, created_at, updated_at) VALUES
                                                                                    (1, '아메리카노', 2000, NOW(), NOW()),
                                                                                    (2, '카페라떼', 2500, NOW(), NOW()),
                                                                                    (3, '카푸치노', 3000, NOW(), NOW()),
                                                                                    (4, '카라멜 마끼아또', 3500, NOW(), NOW()),
                                                                                    (5, '에스프레소', 1800, NOW(), NOW()),
                                                                                    (6, '바닐라 라떼', 3200, NOW(), NOW()),
                                                                                    (7, '녹차 라떼', 3300, NOW(), NOW()),
                                                                                    (8, '콜드브루', 2700, NOW(), NOW()),
                                                                                    (9, '모카라떼', 3400, NOW(), NOW()),
                                                                                    (10, '디카페인 아메리카노', 2100, NOW(), NOW());


INSERT INTO product_stock (product_id, quantity,version) VALUES (1, 1000,0),
                                                                (2, 1000,0),
                                                                (3, 1000,0),
                                                                (4, 1000,0),
                                                                (5, 1000,0),
                                                                (6, 1000,0),
                                                                (7, 1000,0),
                                                                (8, 1000,0),
                                                                (9, 1000,0),
                                                                (10, 1000,0);

delete from product_stock where id in (1,2,3,4,5,6,7,8,9,10);
