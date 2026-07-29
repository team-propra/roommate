CREATE TABLE user_role (
    user_handle VARCHAR(100) NOT NULL REFERENCES users(handle) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_handle, role),
    CONSTRAINT user_role_known_role CHECK (role IN ('USER', 'VERIFIED_USER', 'ADMIN'))
);

-- Preserve every role that is still present in the legacy single-role column.
-- A previously overwritten ADMIN role cannot be inferred safely from a
-- VERIFIED_USER row, so it is deliberately not reconstructed here.
INSERT INTO user_role (user_handle, role)
SELECT handle, role
FROM users
WHERE role IN ('USER', 'VERIFIED_USER', 'ADMIN');

ALTER TABLE users DROP COLUMN role;
