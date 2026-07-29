ALTER TABLE user_role DROP CONSTRAINT user_role_known_role;

ALTER TABLE user_role
    ADD CONSTRAINT user_role_known_role
    CHECK (role IN ('USER', 'VERIFIED_USER', 'ADMIN', 'INJECTED_ADMIN'));
