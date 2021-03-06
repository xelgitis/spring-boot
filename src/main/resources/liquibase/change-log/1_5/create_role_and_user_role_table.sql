ALTER TABLE Users DROP COLUMN ROLE;

CREATE TABLE ROLE (
  id NUMBER NOT NULL,
  role varchar2(45) NOT NULL,
  CONSTRAINT pk_role PRIMARY KEY(id)
);
  
CREATE SEQUENCE role_increment MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 1 START WITH 1 NOCACHE;

CREATE TABLE USER_ROLE (
  id NUMBER NOT NULL,
  user_id NUMBER NOT NULL,
  role_id NUMBER NOT NULL,
  CONSTRAINT pk_user_role PRIMARY KEY(id)
);
  
CREATE SEQUENCE user_role_increment MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 1 START WITH 1 NOCACHE;

ALTER TABLE USER_ROLE ADD CONSTRAINT userid_uq UNIQUE (user_id);

ALTER TABLE USER_ROLE ADD CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES USERS(id);
ALTER TABLE USER_ROLE ADD CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES ROLE(id);

