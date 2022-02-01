CREATE TABLE APP_USER(
   user_id SERIAL PRIMARY KEY,
   username varchar(10) not null,
   password varchar(255) not null,
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_changetimestamp_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_last_modified_date BEFORE UPDATE
    ON APP_USER FOR EACH ROW EXECUTE PROCEDURE
    update_changetimestamp_column();