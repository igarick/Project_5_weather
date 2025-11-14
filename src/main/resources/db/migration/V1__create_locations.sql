CREATE TABLE IF NOT EXISTS locations
(
    ID        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name      VARCHAR(50) not null,
    UserId    SERIAL      not null,
    Latitude  DECIMAL(9, 6),
    Longitude DECIMAL(9, 6),
    FOREIGN KEY (UserId) REFERENCES users (ID)
);