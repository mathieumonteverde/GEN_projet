/*
   Drop the database if it already exists and create it again.
*/

DROP DATABASE IF EXISTS gen;
CREATE DATABASE gen;
USE gen;


# Users table
CREATE TABLE User (
   username VARCHAR(30),
   password VARCHAR(30), # TODO engrypt the passwords
   mail VARCHAR(255) UNIQUE,
   role INTEGER,

   PRIMARY KEY(username)
);

# Scores table
CREATE TABLE Score (
   id INTEGER AUTO_INCREMENT,
   raceName VARCHAR(80) NOT NULL,
   position INTEGER NOT NULL,
   time INTEGER NOT NULL, # Time stored as a number of milliseconds ?
   date DATE NOT NULL,
   username VARCHAR(30) NOT NULL,

   PRIMARY KEY(id),
   FOREIGN KEY(username) REFERENCES User(username)
);