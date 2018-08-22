CREATE DATABASE akka_web_api;
USE akka_web_api;

CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL);

CREATE TABLE categories(
	id INT PRIMARY KEY AUTO_INCREMENT,
	parentCategoryId INT,
	name VARCHAR(50) NOT NULL,
	FOREIGN KEY (parentCategoryId) REFERENCES categories(id));

CREATE TABLE posts(
	id INT PRIMARY KEY AUTO_INCREMENT,
	authorId INT NOT NULL,
	postedAt DATETIME NOT NULL,
	message VARCHAR(2048) NOT NULL,
	FOREIGN KEY (authorId) REFERENCES users(id));

CREATE TABLE threads(
	id INT PRIMARY KEY AUTO_INCREMENT,
	categoryId INT NOT NULL,
	rootPostId INT NOT NULL,
	title VARCHAR(100) NOT NULL,
	FOREIGN KEY (categoryId) REFERENCES categories(id),
	FOREIGN KEY (rootPostId) REFERENCES posts(id));

CREATE TABLE threadPosts(
	id INT PRIMARY KEY AUTO_INCREMENT,
	threadId INT NOT NULL,
	postId INT NOT NULL,
	FOREIGN KEY (threadId) REFERENCES threads(id),
	FOREIGN KEY (postId) REFERENCES posts(id));