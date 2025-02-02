CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

CREATE TABLE plants
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    user_id BIGINT       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE sensors
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    type         VARCHAR(255) NOT NULL,
    is_available BOOLEAN      NOT NULL,
    plant_id     BIGINT       NOT NULL,
    user_id      BIGINT       NOT NULL,
    FOREIGN KEY (plant_id) REFERENCES plants (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE readings
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    reading_value DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL,
    sensor_id BIGINT   NOT NULL,
    user_id   BIGINT   NOT NULL,
    FOREIGN KEY (sensor_id) REFERENCES sensors (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE alerts
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    type       VARCHAR(255) NOT NULL,
    message    VARCHAR(255) NOT NULL,
    reading_id BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    FOREIGN KEY (reading_id) REFERENCES readings (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE tokens
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    token      VARCHAR(500) UNIQUE NOT NULL,
    token_type VARCHAR(50)         NOT NULL,
    revoked    BOOLEAN             NOT NULL,
    expired    BOOLEAN             NOT NULL,
    user_id    BIGINT              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


-- Insertar usuarios
INSERT INTO users (email, password, role)
VALUES ('admin@example.com', '$2a$10$B0WJv5cZ2ZVVZC1vlF1.VuVw7OQ8pMN3JjYvqDhfT4L0P8bJ9NQba', 'ADMIN'),
       ( 'user@example.com', '$2a$10$B0WJv5cZ2ZVVZC1vlF1.VuVw7OQ8pMN3JjYvqDhfT4L0P8bJ9NQda', 'USER');

-- Insertar plantas
INSERT INTO plants ( name, user_id)
VALUES ('Planta 1', 1),
       ('Planta 2', 2);

-- Insertar sensores
INSERT INTO sensors (type, is_available, plant_id, user_id)
VALUES ('Temperatura', true, 1, 1),
       ('Humedad', true, 2, 2);

-- Insertar lecturas
INSERT INTO readings (reading_value, timestamp, sensor_id, user_id)
VALUES (25.5, '2024-01-31 12:00:00', 1, 1),
       (60.2, '2024-01-31 12:05:00', 2, 2);

-- Insertar alertas
INSERT INTO alerts ( type, message, reading_id, user_id)
VALUES ( 'ALTA_TEMPERATURA', 'Temperatura elevada detectada', 1, 1),
       ( 'BAJA_HUMEDAD', 'Humedad baja detectada', 2, 2);

-- Insertar tokens
INSERT INTO tokens ( token, token_type, revoked, expired, user_id)
VALUES ('token_admin_123', 'BEARER', false, false, 1),
       ('token_user_456', 'BEARER', false, false, 2);