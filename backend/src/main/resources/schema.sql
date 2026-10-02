-- ========================================================
-- Smart Inventory & Supply Chain Management Schema
-- Database: MySQL 8.0+
-- ========================================================

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    parent_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    status ENUM('ACTIVE', 'INACTIVE', 'MAINTENANCE') DEFAULT 'ACTIVE',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT,
    unit_of_measure ENUM('PIECE', 'KG', 'LITER', 'METER', 'BOX', 'PALLET') DEFAULT 'PIECE',
    weight DECIMAL(15, 2),
    manufacturer VARCHAR(100),
    min_stock_level INT DEFAULT 0,
    max_stock_level INT DEFAULT 10000,
    status ENUM('ACTIVE', 'DISCONTINUED', 'OUT_OF_STOCK') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    role ENUM('ADMIN', 'WAREHOUSE_MANAGER', 'OPERATOR', 'VIEWER') NOT NULL DEFAULT 'OPERATOR',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventory_snapshots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    quantity_on_hand INT NOT NULL DEFAULT 0,
    quantity_reserved INT DEFAULT 0,
    bin_location VARCHAR(50),
    stock_status ENUM('OPTIMAL', 'LOW_STOCK', 'CRITICAL', 'OUT_OF_STOCK') DEFAULT 'OPTIMAL',
    version BIGINT DEFAULT 0,
    last_event_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_prod_wh UNIQUE (product_id, warehouse_id),
    CONSTRAINT chk_snapshot_quantity_non_negative CHECK (quantity_on_hand >= 0),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS inventory_events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    snapshot_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    event_type ENUM('INBOUND', 'OUTBOUND', 'ADJUSTMENT', 'TRANSFER', 'RECOUNT', 'DAMAGE', 'RETURN') NOT NULL,
    quantity_change INT NOT NULL,
    quantity_before INT,
    quantity_after INT,
    reference_number VARCHAR(100) NOT NULL,
    reason TEXT,
    performed_by VARCHAR(100),
    event_timestamp TIMESTAMP NOT NULL,
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (snapshot_id) REFERENCES inventory_snapshots(id) ON DELETE RESTRICT,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE RESTRICT
);

-- Indexes for performance
CREATE INDEX idx_snapshot_product ON inventory_snapshots(product_id);
CREATE INDEX idx_snapshot_warehouse ON inventory_snapshots(warehouse_id);
CREATE INDEX idx_snapshot_status ON inventory_snapshots(stock_status);
CREATE INDEX idx_event_snapshot ON inventory_events(snapshot_id);
CREATE INDEX idx_event_product ON inventory_events(product_id);
CREATE INDEX idx_event_type ON inventory_events(event_type);
CREATE INDEX idx_event_timestamp ON inventory_events(event_timestamp);
CREATE INDEX idx_event_reference ON inventory_events(reference_number);

-- ========================================================
-- Seed Data
-- ========================================================

-- Seed categories
INSERT IGNORE INTO categories (id, name, description) VALUES
(1, 'Sensors', 'Industrial sensors including radar, ultrasonic, and optical sensors'),
(2, 'ECU', 'Electronic Control Units for automotive applications'),
(3, 'Actuators', 'Hydraulic and electric actuators'),
(4, 'Braking Systems', 'Brake components and systems');

-- Seed warehouses
INSERT IGNORE INTO warehouses (id, code, name, location, status, description) VALUES
(1, 'WH-MAIN-01', 'Bosch Central Logistics Hub', 'Ho Chi Minh City, Vietnam', 'ACTIVE', 'Primary distribution center for Vietnam region'),
(2, 'WH-DIST-02', 'High-Tech Industrial Distribution Hub', 'Binh Duong, Vietnam', 'ACTIVE', 'Secondary hub for industrial components');

-- Seed products
INSERT IGNORE INTO products (id, sku, name, description, category_id, unit_of_measure, weight, manufacturer, min_stock_level, max_stock_level, status) VALUES
(1, 'BOSCH-SEN-001', 'Industrial Radar Distance Sensor', 'High-precision radar sensor for industrial automation applications', 1, 'PIECE', 0.45, 'Bosch', 15, 500, 'ACTIVE'),
(2, 'BOSCH-ECU-002', 'Automotive Electronic Control Unit', 'Advanced ECU for engine management systems', 2, 'PIECE', 1.2, 'Bosch', 10, 200, 'ACTIVE'),
(3, 'BOSCH-ACT-003', 'Hydraulic Micro-Actuator 24V', 'Compact hydraulic actuator for precision control', 3, 'PIECE', 0.8, 'Bosch', 20, 300, 'ACTIVE'),
(4, 'BOSCH-BRK-004', 'High-Performance Brake Caliper Pad', 'Premium brake caliper pads for commercial vehicles', 4, 'PIECE', 2.5, 'Bosch', 25, 400, 'ACTIVE');

-- Seed default admin user (password: admin123 - BCrypt encoded)
INSERT IGNORE INTO users (id, username, password, email, full_name, role, is_active) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1JqCQBj1v8dJQvLF5mZxqK8JxJ5qK8u', 'admin@inventory.local', 'System Administrator', 'ADMIN', TRUE),
(2, 'operator1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1JqCQBj1v8dJQvLF5mZxqK8JxJ5qK8u', 'operator@inventory.local', 'Warehouse Operator', 'OPERATOR', TRUE);

-- Seed initial inventory snapshots
INSERT IGNORE INTO inventory_snapshots (id, product_id, warehouse_id, quantity_on_hand, stock_status) VALUES
(1, 1, 1, 45, 'LOW_STOCK'),
(2, 2, 1, 8, 'CRITICAL'),
(3, 3, 1, 120, 'OPTIMAL'),
(4, 4, 1, 14, 'LOW_STOCK');
