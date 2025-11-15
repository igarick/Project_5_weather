CREATE TABLE IF NOT EXISTS project_schema.locations
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    user_id   BIGINT NOT NULL ,
    latitude  DECIMAL(9, 6),
    longitude DECIMAL(9, 6),
    CONSTRAINT fk_locations_user FOREIGN KEY (user_id) REFERENCES project_schema.users (id)
);