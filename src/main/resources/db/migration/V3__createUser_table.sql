CREATE TABLE users(
    handle VARCHAR(100) PRIMARY KEY,
    role VARCHAR(20) NOT NULL,
    key_id UUID,
    keymaster_name VARCHAR(100)
);