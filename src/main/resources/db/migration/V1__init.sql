CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE role (
  id SERIAL PRIMARY KEY,
  name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name varchar(40) NOT NULL,
  username varchar(15) NOT NULL UNIQUE,
  email varchar(40) NOT NULL UNIQUE,
  password varchar(100) NOT NULL
);

CREATE TABLE user_role (
  role_id int NOT NULL,
  user_id int NOT NULL,
  CONSTRAINT user_role_pk PRIMARY KEY (role_id, user_id),
  CONSTRAINT user_role_role_id_fk FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT user_role_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE team (
  id SERIAL PRIMARY KEY,
  name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE player (
  id SERIAL PRIMARY KEY,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  team_id int NOT NULL,
  CONSTRAINT player_team_id_fk FOREIGN KEY (team_id) REFERENCES team (id)
);

INSERT  INTO role(name) VALUES('ROLE_USER');
INSERT  INTO role(name) VALUES('ROLE_ADMIN');