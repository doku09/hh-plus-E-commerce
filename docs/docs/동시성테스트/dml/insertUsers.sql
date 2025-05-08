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

INSERT INTO point (amount, user_id, version)
VALUES
    (1000000, 1, 0),
    (1000000, 2, 0),
    (1000000, 3, 0),
    (1000000, 4, 0),
    (1000000, 5, 0),
    (1000000, 6, 0),
    (1000000, 7, 0),
    (1000000, 8, 0),
    (1000000, 9, 0),
    (1000000, 10, 0);