ALTER TABLE project_schema.locations
    ALTER COLUMN latitude TYPE DECIMAL(17, 14),
    ALTER COLUMN longitude TYPE DECIMAL(17, 14);