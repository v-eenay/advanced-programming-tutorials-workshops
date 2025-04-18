-- Database initialization script

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS user_db;

-- Use the database
USE user_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL, -- Increased size for BCrypt hashes
                                     role ENUM('admin', 'user') NOT NULL DEFAULT 'user',
                                     profile_picture MEDIUMBLOB
);

-- Note: The passwords below will be hashed by the application when users log in for the first time

-- Insert sample admin user if not exists
INSERT INTO users (name, email, password, role)
SELECT 'Admin User', 'admin@example.com', 'admin123', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');

-- Insert sample regular user if not exists
INSERT INTO users (name, email, password, role)
SELECT 'Student User', 'student@example.com', 'student123', 'user'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student@example.com');
