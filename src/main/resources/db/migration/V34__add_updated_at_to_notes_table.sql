ALTER TABLE notes
ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();
