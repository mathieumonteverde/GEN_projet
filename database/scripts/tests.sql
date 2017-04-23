/*
   Fichier de tests d'insertion, modification, suppression de données 
   dans la DB gen.
*/

USE gen;

# User insertion
CALL addUser('Valomat', '1234', 'Valomat@gmail.com', 1);
CALL addUser('FlyingDutchMan', '1234', 'fdm@gmail.coymaembm', 2);
CALL addUser('chaymaemb', '1234', 'cha@gmail.com', 2);

# Score insertion
CALL addScore('Fire Dome', 1, 607, '2017-04-23', 'Valomat');
CALL addScore('Blue Lagoon', 3, 1057, '2017-04-23', 'Valomat');
CALL addScore('Fire Dome', 2, 707, '2017-04-23', 'FlyingDutchMan');
CALL addScore('Blue Lagoon', 1, 700, '2017-04-23', 'FlyingDutchMan');