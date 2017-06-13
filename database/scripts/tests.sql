/*
   Fichier de tests d'insertion, modification, suppression de données 
   dans la DB gen.
   
   This files adds sample users and score and tests the insert, update 
   and delete procedures.
*/

USE gen;

# User insertion
CALL insertUser('Valomat', '1234', 1);
CALL insertUser('FlyingDutchMan', '1234', 2);
CALL insertUser('Chaymaemb', '1234', 2);

# Wrong username
CALL userAuthentification('valomat', '1234');
# Wrong password
CALL userAuthentification('Valomat', '1235');
# Correct username and password
CALL userAuthentification('Valomat', '1234');

# Score insertion
CALL insertScore('Fire Dome', 1, 607, '2017-04-23', 'Valomat');
CALL insertScore('Blue Lagoon', 3, 1057, '2017-04-23', 'Valomat');
CALL insertScore('Fire Dome', 2, 707, '2017-04-23', 'FlyingDutchMan');
CALL insertScore('Blue Lagoon', 1, 700, '2017-04-23', 'FlyingDutchMan');
CALL insertScore('Fire Dome', 3, 1000, '2017-04-23', 'Chaymaemb');
CALL insertScore('Blue Lagoon', 2, 907, '2017-04-23', 'Chaymaemb');

# User password modification
CALL updateUserPassword('Valomat', '1235');

# Correct username and  now incorrect password
CALL userAuthentification('Valomat', '1234');
# Correct username and password
CALL userAuthentification('Valomat', '1235');

# Delete Score of ID 1
CALL deleteScoreByID(1);
# Display changes
CALL getScores();

# Delete a user, it should also delete its scores
CALL deleteUser('Valomat');
# Display changes
SELECT * FROM User;