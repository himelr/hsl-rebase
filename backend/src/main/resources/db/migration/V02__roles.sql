CREATE TABLE ROLES(
   role_id SERIAL PRIMARY KEY,
   role smallint not null check ( role > 0 ),
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER update_last_modified_date BEFORE UPDATE
    ON ROLES FOR EACH ROW EXECUTE PROCEDURE
    update_changetimestamp_column();

ALTER TABLE APP_USER ADD COLUMN email varchar(50) not null default '';