-- Run these examples against planet.sqlite to see how SELECT ... FROM works.

-- Select all columns and all rows
SELECT * FROM Planet;

-- Select only some columns
SELECT id, name FROM Planet;

-- Use an expression over a column value
SELECT 'Planet ' || name FROM Planet;
