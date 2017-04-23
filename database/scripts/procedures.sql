/*
   Procédures d'insertion de données dans la DB Gen
*/

USE gen;


/*
   Procedure to add a user
*/
DELIMITER $$
CREATE PROCEDURE addUser (username VARCHAR(30), password VARCHAR(30), mail VARCHAR(255), role INTEGER)
BEGIN
   INSERT INTO User (username, password, mail, role)
      VALUES(username, password, mail, role);
END
$$

/*
   Procedure to add a Score
*/
DELIMITER $$
CREATE PROCEDURE addScore (raceName VARCHAR(80), position INTEGER, time INTEGER, date DATE, username VARCHAR(30))
BEGIN
   INSERT INTO Score (raceName, position, time, date, username)
      VALUES(raceName, position, time, date, username);
END
$$