CREATE TABLE opportunities (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    opportunity_name VARCHAR(255) NOT NULL,
    opportunity_value DECIMAL(15, 2) NOT NULL,
    stage VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
