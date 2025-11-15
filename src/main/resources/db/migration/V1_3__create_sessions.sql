CREATE TABLE IF NOT EXISTS sessions
(
    id         UUID PRIMARY KEY,
    user_id    BIGINT,
    expires_at TIMESTAMP,
    CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users (id)
)