CREATE DATABASE blackjacks_db;
USE blackjacks_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- This is for default account get 1000
ALTER TABLE users
ALTER COLUMN credits SET DEFAULT 1000;
