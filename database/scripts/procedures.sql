/*
   Procédures d'insertion, modifications et suppression de données dans la DB Gen
*/
USE gen;

/*
   ========================================================
   INSERTION
   ========================================================
*/
# Insertion d'un utilisateur
DELIMITER $$
CREATE PROCEDURE insertUser (username VARCHAR(30), password VARCHAR(30), mail VARCHAR(255), role INTEGER)
BEGIN
   SET @salt = SHA2(RAND(), 224);
   SET @hash = SHA2(CONCAT(@salt, password), 512);
   SET @storedValue = CONCAT(@salt, @hash);
   INSERT INTO User (username, password, mail, role)
      VALUES(username, @storedValue, mail, role);
END
$$

# Insertion d'un score
DELIMITER $$
CREATE PROCEDURE insertScore (raceName VARCHAR(80), position INTEGER, time INTEGER, date DATE, username VARCHAR(30))
BEGIN
   INSERT INTO Score (raceName, position, time, date, username)
      VALUES(raceName, position, time, date, username);
END
$$

/*
   ========================================================
   MODIFICATION
   ========================================================
*/
# Modification de l'adresse email d'un utilisateur
DELIMITER $$
CREATE PROCEDURE updateUserMail (username VARCHAR(30), mail VARCHAR(255))
BEGIN
   UPDATE User u
   SET u.mail = mail
   WHERE u.username = username;
END
$$

# Modification du mot de passe d'un utilisateur
DELIMITER $$
CREATE PROCEDURE updateUserPassword (username VARCHAR(30), password VARCHAR(30))
BEGIN
   SET @salt = SHA2(RAND(), 224);
   SET @hash = SHA2(CONCAT(@salt, password), 512);
   SET @storedValue = CONCAT(@salt, @hash);
   UPDATE User u
   SET u.password = @storedValue
   WHERE u.username = username;
END
$$

# Modification du rôle d'un utilisateur
DELIMITER $$
CREATE PROCEDURE updateUserRole (username VARCHAR(30), role INTEGER)
BEGIN
   UPDATE User u
   SET u.role = role
   WHERE u.username = username;
END
$$


/*
   ========================================================
   Suppression
   ========================================================
*/
# Suppression d'un utilisateur
DELIMITER $$
CREATE PROCEDURE deleteUser (username VARCHAR(30))
BEGIN
   DELETE FROM User
   WHERE User.username = username;
END
$$

# Suppression d'un score en fonction de son ID
DELIMITER $$
CREATE PROCEDURE deleteScoreById (id INTEGER)
BEGIN
   DELETE FROM Score
   WHERE Score.id = id;
END
$$


/*
   ========================================================
   Authentification des utilisateurs
   ========================================================
*/

DELIMITER $$
CREATE PROCEDURE userAuthentification (username VARCHAR(30), password VARCHAR(30))
BEGIN
   
   # Récupérer le mot de passe hashé
   SET @storedHash = (SELECT User.password
               FROM User
               WHERE User.username = username);

   # Récupérer le sel et calculer le hash avec le mot de passe proposé
   SET @salt = SUBSTRING(@storedHash, 1, 56);
   SET @hash = SHA2(CONCAT(@salt, password), 512);
   SET @compareHash = CONCAT(@salt, @hash);

   # Chercher l'utilisateur correspondant
   SELECT User.username
   FROM User
   WHERE User.username LIKE BINARY username and User.password = @compareHash;
END
$$