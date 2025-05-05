
-- ========================
-- MAIN TABLES
-- ========================

CREATE TABLE Clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
	rut VARCHAR(20)
);

CREATE TABLE Dealers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
	rut VARCHAR(20),
    email VARCHAR(100),
    phone VARCHAR(20),
    vehicle VARCHAR(50),
    plate VARCHAR(20)
);

CREATE TABLE Products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    stock INT,
    price FLOAT,
    category VARCHAR(100),
    supplier VARCHAR(100)
);

CREATE TABLE Orders (
    id SERIAL PRIMARY KEY,
    order_date TIMESTAMP,
	delivery_date TIMESTAMP,
    status VARCHAR(50),
    total_price INT,
	dealer_id INT,
	client_id INT,
    FOREIGN KEY (client_id) REFERENCES Clients(id),
	FOREIGN KEY(dealer_id) REFERENCES Dealers(id)
);

CREATE TABLE Order_Products (
    product_id INT,
    order_id INT,
    PRIMARY KEY (product_id, order_id),
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (order_id) REFERENCES Orders(id)
);

-- ========================
-- COMPANIES AND DRIVERS
-- ========================



CREATE TABLE Companies (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);

CREATE TABLE Company_Driver (
    company_id INT,
    dealer_id INT,
    PRIMARY KEY (company_id, dealer_id),
    FOREIGN KEY (company_id) REFERENCES Companies(id),
    FOREIGN KEY (dealer_id) REFERENCES Dealers(id)
);

-- ========================
-- ADDITIONAL TABLES
-- ========================

CREATE TABLE Rating (
    id SERIAL PRIMARY KEY,
    dealer_id INT,
    date DATE,
    client_id INT,
    rating INT,
    comment TEXT,
    FOREIGN KEY (dealer_id) REFERENCES Dealers(id),
    FOREIGN KEY (client_id) REFERENCES Clients(id)
);

CREATE TABLE Payment_Method (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50)
);

CREATE TABLE Order_Details (
    id SERIAL PRIMARY KEY,
    order_id INT,
    payment_method VARCHAR(20),
    total_products INT,
    price INT,
    FOREIGN KEY (order_id) REFERENCES Orders(id)
);

CREATE TABLE Company_PaymentMethod (
    company_id INT,
    payment_method_id INT,
    PRIMARY KEY (company_id, payment_method_id),
    FOREIGN KEY (company_id) REFERENCES Companies(id),
    FOREIGN KEY (payment_method_id) REFERENCES Payment_Method(id)
);
