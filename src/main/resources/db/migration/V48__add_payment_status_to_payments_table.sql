ALTER TABLE payments
ADD COLUMN payment_status VARCHAR(50) NOT NULL DEFAULT 'pending';
