-- ========================================================
-- Smart Inventory & Supply Chain Management Schema
-- Database: MySQL 8.0+
-- ========================================================

CREATE TABLE IF NOT EXISTS warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(60) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    unit VARCHAR(30) DEFAULT 'unit',
    price DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    low_stock_threshold INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_products_price_non_negative CHECK (price >= 0.00),
    CONSTRAINT chk_products_threshold_non_negative CHECK (low_stock_threshold >= 0)
);

CREATE TABLE IF NOT EXISTS inventory_snapshots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_prod_wh UNIQUE (product_id, warehouse_id),
    CONSTRAINT chk_stock_quantity_non_negative CHECK (quantity >= 0),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS inventory_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    event_type ENUM('INBOUND', 'OUTBOUND', 'ADJUSTMENT', 'DAMAGE') NOT NULL,
    quantity_delta INT NOT NULL,
    balance_after INT NOT NULL,
    reference_number VARCHAR(100) NOT NULL,
    reason VARCHAR(255),
    performed_by VARCHAR(100) NOT NULL,
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    role ENUM('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed initial warehouse
INSERT IGNORE INTO warehouses (id, code, name, location) VALUES
(1, 'WH-MAIN-01', 'Bosch Central Logistics Hub', 'Ho Chi Minh City, Vietnam'),
(2, 'WH-DIST-02', 'High-Tech Industrial Distribution Hub', 'Binh Duong, Vietnam');

-- Seed initial products
INSERT IGNORE INTO products (id, sku, name, category, unit, price, low_stock_threshold) VALUES
(1, 'BOSCH-SEN-001', 'Industrial Radar Distance Sensor', 'Sensors', 'unit', 185.50, 15),
(2, 'BOSCH-ECU-002', 'Automotive Electronic Control Unit', 'ECU', 'unit', 450.00, 10),
(3, 'BOSCH-ACT-003', 'Hydraulic Micro-Actuator 24V', 'Actuators', 'unit', 92.00, 20),
(4, 'BOSCH-BRK-004', 'High-Performance Brake Caliper Pad', 'Braking Systems', 'pair', 65.00, 25);

-- Seed initial inventory snapshot
INSERT IGNORE INTO inventory_snapshots (id, product_id, warehouse_id, quantity) VALUES
(1, 1, 1, 45),
(2, 2, 1, 8),   -- Low stock trigger candidate
(3, 3, 1, 120),
(4, 4, 1, 14);  -- Low stock trigger candidate
