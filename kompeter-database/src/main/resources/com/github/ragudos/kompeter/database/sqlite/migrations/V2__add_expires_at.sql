ALTER TABLE sessions
ADD COLUMN expires_at DATETIME DEFAULT (DATETIME('now', '+1 hour'));

