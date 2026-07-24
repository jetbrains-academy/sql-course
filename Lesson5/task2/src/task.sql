-- ========== Query 1 ==========
-- Find the average radius of the inhabited planets.
SELECT AVG(radius) AS avg_radius
FROM Planet
WHERE is_inhabited;

-- ========== Query 2 ==========
-- Count how many DISTINCT planets the spacecraft 'Pegasus' has visited
-- (a planet counts once even if Pegasus flew to it several times).
SELECT COUNT(DISTINCT P.id) AS planets
FROM Planet P JOIN Flight F ON P.id = F.planet_id
              JOIN Spacecraft S ON S.id = F.spacecraft_id
WHERE S.name = 'Pegasus';

-- ========== Query 3 ==========
-- Find the name of the planet with the largest radius. Use a scalar subquery.
SELECT name
FROM Planet
WHERE radius = (SELECT MAX(radius) FROM Planet);
