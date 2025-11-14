CREATE TABLE IF NOT EXISTS users
(
    ID       SERIAL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Login    VARCHAR(50)  NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL
);

-- CREATE TABLE IF NOT EXISTS locations
-- (
--     ID        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--     Name      VARCHAR(50) not null,
--     UserId    SERIAL         not null,
--     Latitude  DECIMAL(9, 6),
--     Longitude DECIMAL(9, 6),
--     FOREIGN KEY (UserId) REFERENCES users (ID)
-- );
--
-- CREATE TABLE IF NOT EXISTS sessions
-- (
--     ID        UUID PRIMARY KEY NOT NULL,
--     UserId    SERIAL              NOT NULL,
--     ExpiresAt TIMESTAMP,
--     FOREIGN KEY (UserId) REFERENCES users (ID)
-- )