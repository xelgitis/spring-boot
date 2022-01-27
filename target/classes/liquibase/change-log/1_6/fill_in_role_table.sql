UPDATE role SET id = role_increment.nextval;

INSERT INTO role (id, role) VALUES (role_increment.nextval, 'user');
INSERT INTO role (id, role) VALUES (role_increment.nextval, 'administrator');