-- Run these against marsoflot.sqlite to see aggregate functions and scalar subqueries.

-- Scalar aggregates
SELECT MAX(radius) FROM Planet;
SELECT AVG(radius / 1.609) FROM Planet WHERE climate = 'mild';

-- COUNT of a constant vs a nullable column (aggregates ignore NULL)
SELECT COUNT(id), COUNT(42) FROM Planet;
SELECT COUNT(cargo), COUNT(*) FROM Flight;

-- DISTINCT: flights vs distinct planets visited by Pegasus
SELECT COUNT(*), COUNT(DISTINCT P.id)
FROM Planet P JOIN Flight F ON P.id = F.planet_id
              JOIN Spacecraft S ON S.id = F.spacecraft_id
WHERE S.name = 'Pegasus';

-- Scalar subquery: the planet with the maximum radius
SELECT * FROM Planet WHERE radius = (SELECT MAX(radius) FROM Planet);
