ALTER TABLE roles add COLUMN user_id integer not null;

ALTER TABLE roles add constraint fk_user
FOREIGN KEY (user_id)
REFERENCES app_user(user_id);