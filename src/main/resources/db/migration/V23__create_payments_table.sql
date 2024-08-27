CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    invoice_id INT NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);
