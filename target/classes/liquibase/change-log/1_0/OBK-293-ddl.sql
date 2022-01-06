CREATE TABLE users (
	username varchar2(45) NOT NULL,
	password varchar2(45) NOT NULL,
	password_salt varchar2(64) NOT NULL,
	name varchar2(45) NOT NULL,
	email varchar2(45) NOT NULL,
	role varchar2(45) NOT NULL,
	CONSTRAINT pk_users PRIMARY KEY (username)
);
