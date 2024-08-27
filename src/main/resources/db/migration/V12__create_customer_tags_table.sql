CREATE TABLE customer_tags (
    customer_id INT NOT NULL,
    tag_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id),
    PRIMARY KEY (customer_id, tag_id)
);
