CREATE TABLE interactions (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    interaction_type VARCHAR(50) NOT NULL,
    interaction_date TIMESTAMP NOT NULL,
    details TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
