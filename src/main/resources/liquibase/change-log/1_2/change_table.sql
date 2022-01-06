DROP TABLE USERS;

CREATE TABLE USERS (
  id NUMBER NOT NULL,
  username varchar2(45) NOT NULL,
  password varchar2(128),
  password_salt varchar2(64),
  address varchar2(64),
  name varchar2(45),
  email varchar2(64),
  role varchar2(45),
  registration_time date,
  CONSTRAINT pk_users PRIMARY KEY(id)
);
  
CREATE SEQUENCE users_increment MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 1 START WITH 1 NOCACHE;