CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    note_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
