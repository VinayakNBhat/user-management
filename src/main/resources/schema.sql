CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    roles varchar(255)
)ENGINE=INNODB;