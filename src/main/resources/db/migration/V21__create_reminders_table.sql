CREATE TABLE reminders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    reminder_date TIMESTAMP NOT NULL,
    reminder_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
