CREATE DATABASE IF NOT EXISTS assignment5;
USE assignment5;
CREATE TABLE IF NOT EXISTS users (
 id VARCHAR(50) PRIMARY KEY, pwd VARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS bus_pass (
 pass_id VARCHAR(20) PRIMARY KEY, reg_no VARCHAR(20),
 student_name VARCHAR(60), route_no INT, amount DOUBLE
);
