CREATE TABLE users(
    key_id UUID PRIMARY KEY,
    role VARCHAR(20) NOT NULL,
    handle VARCHAR(100) NOT NULL,
    keymaster_name VARCHAR(100) NOT NULL
);