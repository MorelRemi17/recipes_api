DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(250) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL
);

INSERT INTO users (name, email, password)
VALUES ('Alex Dupont', 'alexdupont@mail.com', 'alexpassword'),
       ('Marie Curie', 'mariecurie@mail.com', 'mariepassword'),
       ('Jean Durand', 'jeandurand@mail.com', 'jeanpassword');
