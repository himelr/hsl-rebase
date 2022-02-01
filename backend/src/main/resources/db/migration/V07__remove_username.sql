ALTER TABLE app_user
    DROP COLUMN username;

ALTER TABLE app_user ADD CONSTRAINT unique_email UNIQUE (email);
