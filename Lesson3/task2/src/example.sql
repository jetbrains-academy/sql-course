-- Run these examples against L3_planet.sqlite to see how the WHERE clause filters rows.

-- Keep only the rows that match a condition
SELECT id, name
FROM Planet
WHERE id = 1;

-- A more complex condition
SELECT id, name
FROM Planet
WHERE id = 1 OR id = 2 OR name = 'Pyros';

-- NOT with a boolean column
-- (rows where is_inhabited is NULL would be excluded, too)
SELECT name
FROM Planet
WHERE NOT is_inhabited;
