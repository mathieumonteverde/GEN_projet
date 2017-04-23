/*
   Drop the database if it already exists and create it again.
*/

DROP DATABASE IF EXISTS gen;
CREATE DATABASE gen;
USE gen;

# Scores table
CREATE TABLE Score (
   id INTEGER AUTO_INCREMENT,
   raceName VARCHAR(80) NOT NULL,
   position INTEGER NOT NULL,
   time INTEGER NOT NULL, # Time stored as a number of milliseconds ?
   date DATE NOT NULL,

   PRIMARY KEY(id)
);


# Users table
CREATE TABLE User (
   username VARCHAR(30),
   password VARCHAR(30), # TODO engrypt the passwords
   mail VARCHAR(255) UNIQUE,
   role INTEGER,
   scoreId INTEGER NOT NULL,

   PRIMARY KEY(username),
   FOREIGN KEY(scoreId) REFERENCES Score(id)
);