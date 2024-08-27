CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    contact_name VARCHAR(100) NOT NULL,
    contact_email VARCHAR(100) UNIQUE,
    contact_phone VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
