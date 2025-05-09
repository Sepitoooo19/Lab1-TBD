-- ========================
-- MAIN TABLES
-- ========================

-- Tabla: users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('ADMIN', 'CLIENT', 'DEALER')) NOT NULL
);

-- Tabla: companies
CREATE TABLE companies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Tabla: payment_methods
CREATE TABLE payment_methods (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

-- ========================
-- PRODUCTS & ORDERS
-- ========================

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    price FLOAT NOT NULL,
    category VARCHAR(50),
    company_id INT,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_date TIMESTAMP,
    delivery_date TIMESTAMP,
    status VARCHAR(50),
    client_id INT,
    dealer_id INT,
    total_price FLOAT
);

CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    order_id INT UNIQUE,
    payment_method VARCHAR(50),
    total_products INT,
    price FLOAT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- ========================
-- USERS (EXTENDED PROFILES)
-- ========================

-- Tabla: clients
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    rut VARCHAR(20),
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla: dealers
CREATE TABLE dealers (
    id SERIAL PRIMARY KEY,
    rut VARCHAR(20),
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    vehicle VARCHAR(50),
    plate VARCHAR(20),
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ========================
-- RATINGS
-- ========================

ALTER TABLE orders
    ADD FOREIGN KEY (client_id) REFERENCES clients(id),
    ADD FOREIGN KEY (dealer_id) REFERENCES dealers(id);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    rating INT NOT NULL,
    comment TEXT,
    date DATE,
    client_id INT,
    dealer_id INT,
    order_id INT,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (dealer_id) REFERENCES dealers(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- ========================
-- RELATIONAL TABLES (N:N)
-- ========================

-- Relación empresas-medios de pago
CREATE TABLE company_payment_methods (
    company_id INT,
    payment_method_id INT,
    PRIMARY KEY (company_id, payment_method_id),
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

-- Relación pedidos-productos
CREATE TABLE order_products (
    order_id INT,
    product_id INT,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Triggers:
-- Insertar automáticamente la fecha de entrega al marcar como entregado.

-- Trigger function
CREATE OR REPLACE FUNCTION set_delivery_date_when_delivered()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'ENTREGADO' AND NEW.delivery_date IS NULL THEN
        NEW.delivery_date := NOW();
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_set_delivery_date
    BEFORE UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION set_delivery_date_when_delivered();
