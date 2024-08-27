CREATE TABLE activities (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    customer_id INT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_date TIMESTAMP NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
