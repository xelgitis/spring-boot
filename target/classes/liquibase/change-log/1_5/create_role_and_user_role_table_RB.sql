DROP SEQUENCE role_increment; 
DROP SEQUENCE user_role_increment; 

DROP TABLE USER_ROLE;
DROP TABLE ROLE;

ALTER TABLE USERS ADD ROLE varchar2(45); --add role column back