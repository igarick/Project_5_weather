CREATE TABLE IF NOT EXISTS project_schema.sessions
(
    id         UUID PRIMARY KEY,
    user_id    BIGINT NOT NULL ,
    expires_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES project_schema.users (id)
);