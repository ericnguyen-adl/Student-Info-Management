drop user webstudent@localhost;
flush privileges;

CREATE USER 'webstudent'@'localhost' IDENTIFIED BY 'webstudent';
GRANT ALL PRIVILEGES ON * . * TO 'webstudent'@'localhost';
alter user 'webstudent'@'localhost' identified with mysql_native_password by 'webstudent';
