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

CREATE TABLE companies (
    id SERIAL PRIMARY KEY,          -- Identificador único de la compañía
    name VARCHAR(100) NOT NULL,     -- Nombre de la compañía
    email VARCHAR(100),             -- Correo electrónico de la compañía
    phone VARCHAR(20),              -- Teléfono de la compañía
    address VARCHAR(255),           -- Dirección de la compañía
    rut VARCHAR(20),                -- RUT de la compañía
    type VARCHAR(50),               -- Tipo de compañía
    deliveries INT DEFAULT 0,       -- Total de entregas realizadas
    failed_deliveries INT DEFAULT 0,-- Total de entregas fallidas
    total_sales FLOAT DEFAULT 0     -- Total de ventas realizadas
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

-- ========================
-- PROCEDIMIENTOS ALMACENADOS
-- ========================

-- 1 y 3 Registrar un pedido completo y descuenta el stock.
CREATE OR REPLACE PROCEDURE register_order_with_products(
    p_order_date TIMESTAMP,
    p_status VARCHAR,
    p_client_id INT,
    p_dealer_id INT,
    p_total_price FLOAT,
    p_product_ids INT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
v_order_id INT;
    v_product_id INT;
BEGIN
    -- 1. Insert order
INSERT INTO orders (order_date, status, client_id, dealer_id, total_price)
VALUES (p_order_date, p_status, p_client_id, p_dealer_id, p_total_price)
    RETURNING id INTO v_order_id;

-- 2. Loop through product IDs
FOREACH v_product_id IN ARRAY p_product_ids LOOP
        -- a) Insert into order_products
        INSERT INTO order_products (order_id, product_id)
        VALUES (v_order_id, v_product_id);

        -- b) Reduce stock
UPDATE products
SET stock = stock - 1
WHERE id = v_product_id AND stock > 0;

-- Optional: raise exception if stock is insufficient
IF NOT FOUND THEN
            RAISE EXCEPTION 'Insufficient stock for product ID %', v_product_id;
END IF;
END LOOP;
END;
$$;

-- ========================
-- TRIGGERS
-- ========================

-- 1. Insertar automáticamente la fecha de entrega al marcar como entregado.
-- Trigger function:
CREATE OR REPLACE FUNCTION set_delivery_date_when_delivered()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'ENTREGADO' AND NEW.delivery_date IS NULL THEN
        NEW.delivery_date := NOW();
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_set_delivery_date
    BEFORE UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION set_delivery_date_when_delivered();
