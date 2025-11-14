CREATE TABLE IF NOT EXISTS sessions
(
    ID        UUID PRIMARY KEY NOT NULL,
    UserId    SERIAL           NOT NULL,
    ExpiresAt TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES users (ID)
)