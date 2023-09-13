CREATE TABLE Planet(
    id INT PRIMARY KEY,
    name TEXT,
    is_inhabited BIT,
    climate TEXT CHECK climate IN ('cold', 'mild', 'warm', 'hot', 'extremely hot'),
    has_weapons BIT,
    radius NUMERIC(10, 2)
);