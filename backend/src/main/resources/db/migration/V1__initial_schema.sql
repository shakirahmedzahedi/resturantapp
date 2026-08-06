CREATE TABLE app_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(150) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE order_positions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_code VARCHAR(30) NOT NULL UNIQUE,
    position_name VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(30) NOT NULL UNIQUE,
    name_en VARCHAR(150),
    name_bn VARCHAR(150) NOT NULL,
    price DECIMAL(10,2),
    display_order INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE daily_token_sequence (
    business_date DATE PRIMARY KEY,
    last_token INT NOT NULL
);

CREATE TABLE restaurant_orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token_number INT NOT NULL,
    business_date DATE NOT NULL,
    position_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    version_number BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_order_position FOREIGN KEY (position_id) REFERENCES order_positions(id),
    CONSTRAINT fk_order_user FOREIGN KEY (created_by) REFERENCES app_users(id),
    CONSTRAINT uk_daily_token UNIQUE (business_date, token_number)
);

CREATE INDEX idx_orders_business_date_status
    ON restaurant_orders (business_date, status, created_at);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_order FOREIGN KEY (order_id)
        REFERENCES restaurant_orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);
