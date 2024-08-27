CREATE TABLE sales_targets (
    id SERIAL PRIMARY KEY,
    sales_rep_id INT NOT NULL,
    target_value DECIMAL(15, 2) NOT NULL,
    target_period VARCHAR(20) NOT NULL,
    FOREIGN KEY (sales_rep_id) REFERENCES sales_representatives(id)
);
