-- Create roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email varchar(200) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create user_roles table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Create quote_sources table
CREATE TABLE quote_sources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    api_url VARCHAR(255) NOT NULL UNIQUE,
    api_key VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);

-- Create quote_ratings table
CREATE TABLE quote_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    quote_text TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    quote_hash VARCHAR(255) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert default roles
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Insert default users (passwords should be hashed in real applications)
INSERT INTO users (username,email, password) VALUES ('charan','charantej@gmail.com', 'charan');
INSERT INTO users (username,email, password) VALUES ('morten','morten@gmail,com', 'morten');

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- Insert default quote sources
INSERT INTO quote_sources (name, api_url, api_key, is_active) VALUES ('Simpsons', 'https://thesimpsonsquoteapi.glitch.me/quotes', NULL, TRUE);
INSERT INTO quote_sources (name, api_url, api_key, is_active) VALUES ('Ninja', 'https://api.api-ninjas.com/v1/quotes', '0owcCReQcp+ENZXcKVJBNw==ydZpvsKjqEg4WDND', TRUE);
INSERT INTO quote_sources (name, api_url, api_key, is_active) VALUES ('ZenQuotes', 'https://zenquotes.io/api/random',NULL, TRUE);

