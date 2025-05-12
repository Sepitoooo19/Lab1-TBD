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
-- CONSULTAS SQL
-- ========================

--- CONSULTA COMPLEJA 1---
SELECT c.id, c.name, c.rut, c.email, c.phone, c.address, SUM(o.total_price) AS total_spent
FROM orders o
         JOIN clients c ON o.client_id = c.id
WHERE o.status = 'ENTREGADO'
GROUP BY c.id, c.name, c.rut, c.email, c.phone, c.address
ORDER BY total_spent DESC
    LIMIT 1

--- CONSULTA COMPLEJA 2---
SELECT
    p.category,
    p.name AS product_name,
    COUNT(op.product_id) AS product_count
FROM
    products p
        JOIN
    order_products op ON p.id = op.product_id
        JOIN
    orders o ON o.id = op.order_id
WHERE
        o.order_date >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
  AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)
GROUP BY
    p.category, p.name
ORDER BY
    p.category, product_count DESC

--- CONSULTA COMPLEJA 3---
SELECT
    c.id,
    c.name,
    c.email,
    c.phone,
    c.address,
    c.rut,
    c.type,
    c.deliveries,
    c.failed_deliveries,
    c.total_sales
FROM
    companies c
ORDER BY
    c.failed_deliveries DESC

--- CONSULTA COMPLEJA 4---
SELECT
    d.id AS dealer_id,
    d.name AS dealer_name,
    AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
FROM
    orders o
        JOIN
    dealers d ON o.dealer_id = d.id
WHERE
        o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
GROUP BY
    d.id, d.name
ORDER BY
    avg_delivery_time_hours ASC;

--- CONSULTA COMPLEJA 5---
SELECT
    d.id AS dealer_id,
    d.name AS dealer_name,
    COUNT(o.id) AS total_deliveries,
    COALESCE(AVG(r.rating), 0) AS avg_rating,
    (COUNT(o.id) * 0.7 + COALESCE(AVG(r.rating), 0) * 0.3) AS performance_score
FROM
    dealers d
        LEFT JOIN
    orders o ON d.id = o.dealer_id AND o.status = 'ENTREGADO'
        LEFT JOIN
    ratings r ON d.id = r.dealer_id
GROUP BY
    d.id, d.name
ORDER BY
    performance_score DESC
    LIMIT 3;


--- CONSULTA COMPLEJA 6---
SELECT
    od.payment_method,
    COUNT(*) AS usage_count
FROM
    orders o
        JOIN
    order_details od ON o.id = od.order_id
WHERE
        o.status = 'URGENTE'
GROUP BY
    od.payment_method
ORDER BY
    usage_count DESC
    LIMIT 1;

-- ========================
-- PROCEDIMIENTOS ALMACENADOS
-- ========================

-- 1 y 3 Registrar un pedido completo y descuenta el stock.
CREATE OR REPLACE PROCEDURE register_order_with_products(
    p_order_date TIMESTAMP,
    p_status VARCHAR,
    p_client_id INT,
    p_product_ids INT[],
    p_dealer_id INT DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
v_order_id INT;
    v_product_id INT;
    v_total_price FLOAT := 0.0;
BEGIN
    -- Calcular el precio total de los productos
SELECT COALESCE(SUM(price), 0)
INTO v_total_price
FROM products
WHERE id = ANY(p_product_ids);

-- Insertar la orden con el total calculado
INSERT INTO orders (order_date, status, client_id, dealer_id, total_price)
VALUES (p_order_date, p_status, p_client_id, p_dealer_id, v_total_price)
    RETURNING id INTO v_order_id;

-- Insertar los productos y actualizar stock
FOREACH v_product_id IN ARRAY p_product_ids LOOP
        INSERT INTO order_products (order_id, product_id)
        VALUES (v_order_id, v_product_id);

UPDATE products
SET stock = stock - 1
WHERE id = v_product_id AND stock > 0;

IF NOT FOUND THEN
            RAISE EXCEPTION 'Sin stock para el producto ID %', v_product_id;
END IF;
END LOOP;
END;
$$;
-- ========================

-- 2 Cambiar el estado de un pedido con validación
CREATE OR REPLACE PROCEDURE change_order_status(
    p_order_id INT,
    p_new_status VARCHAR,
    p_delivery_date TIMESTAMP DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
v_current_status VARCHAR;
BEGIN
    -- Validar que el pedido exista y obtener su estado actual
SELECT status INTO v_current_status
FROM orders
WHERE id = p_order_id;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Pedido con ID % no existe', p_order_id;
END IF;

    -- Validar que aún no esté finalizado
    IF v_current_status IN ('ENTREGADO', 'FALLIDA') THEN
        RAISE EXCEPTION 'El pedido ya ha sido finalizado con estado %', v_current_status;
END IF;

    -- Actualizar el estado y la fecha si corresponde
    IF p_new_status = 'ENTREGADO' THEN
UPDATE orders
SET status = p_new_status,
    delivery_date = COALESCE(p_delivery_date, NOW())
WHERE id = p_order_id;
ELSE
UPDATE orders
SET status = p_new_status
WHERE id = p_order_id;
END IF;
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

-- 2. Registrar una notificación si se detecta un problema crítico en el pedido.
-- Trigger function:
CREATE OR REPLACE FUNCTION log_failed_order()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.status = 'FALLIDA' AND OLD.status IS DISTINCT FROM NEW.status THEN
    RAISE NOTICE 'Pedido % marcado como FALLIDA', NEW.id;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_log_failed_order
    AFTER UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION log_failed_order();

-- 3. Insertar una calificación automática si no se recibe en 48 horas.
-- Trigger function:
CREATE OR REPLACE FUNCTION insert_auto_rating_if_late()
RETURNS TRIGGER AS $$
BEGIN
  -- Verifica si el estado se cambió a ENTREGADO y la entrega fue hace más de 48 horas
  IF NEW.status = 'ENTREGADO'
     AND OLD.status IS DISTINCT FROM NEW.status
     AND NEW.delivery_date IS NOT NULL
     AND NEW.delivery_date <= NOW() - INTERVAL '48 hours' THEN

    -- Verifica si ya existe una calificación para esta orden
    IF NOT EXISTS (
      SELECT 1 FROM ratings WHERE order_id = NEW.id
    ) THEN
      INSERT INTO ratings (rating, comment, date, client_id, dealer_id, order_id)
      VALUES (
        1,
        'Calificación automática: no se recibió calificación en 48h.',
        CURRENT_DATE,
        NEW.client_id,
        NEW.dealer_id,
        NEW.id
      );
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_auto_rating_if_late
    AFTER UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION insert_auto_rating_if_late();
