-- This script will be used to initialize/reinitialize the learner database for this task
DROP TABLE IF EXISTS Planet;
CREATE TABLE Planet(
    id INT PRIMARY KEY,
    name TEXT,
    is_inhabited INTEGER,
    climate TEXT CHECK (climate IN ('cold', 'mild', 'warm', 'hot', 'extremely hot')),
    has_weapons INTEGER,
    radius NUMERIC(10, 2)
);
