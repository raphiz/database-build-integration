CREATE TABLE campaign_customers (
    campaign_id INT NOT NULL,
    customer_id INT NOT NULL,
    FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    PRIMARY KEY (campaign_id, customer_id)
);
