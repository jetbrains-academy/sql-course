-- This script will be used to initialize/reinitialize the learner database for this task
DROP TABLE IF EXISTS Flight;
DROP TABLE IF EXISTS Spacecraft;
DROP TABLE IF EXISTS Planet;
CREATE TABLE Planet(
    id INT PRIMARY KEY,
    name TEXT,
    is_inhabited INTEGER,
    climate TEXT CHECK (climate IN ('cold', 'mild', 'warm', 'hot', 'extremely hot')),
    has_weapons INTEGER,
    radius NUMERIC(10, 2)
);
CREATE TABLE Spacecraft(
    id INT PRIMARY KEY,
    name TEXT,
    capacity INTEGER
);
CREATE TABLE Flight(
    num TEXT,
    planet_id INT,
    flight_date TEXT,
    spacecraft_id INT,
    people_count INTEGER
);
