ALTER TABLE invoices
ADD COLUMN discount DECIMAL(15, 2) NOT NULL DEFAULT 0.00;
