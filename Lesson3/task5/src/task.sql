-- ========== Query 1 ==========
-- Find all uninhabited planets whose climate matches the climate of at least one
-- inhabited planet. Use a subquery together with the IN operator.
SELECT id, name
FROM Planet
WHERE NOT is_inhabited AND climate IN (SELECT climate FROM Planet WHERE is_inhabited);

-- ========== Query 2 ==========
-- Find all inhabited planets for which there exists an uninhabited planet with the
-- same climate. Use a correlated subquery with the EXISTS operator (the alias P
-- refers to the row of the outer query).
SELECT id, name
FROM Planet AS P
WHERE is_inhabited AND EXISTS (SELECT 1 FROM Planet WHERE NOT is_inhabited AND climate = P.climate);

-- ========== Query 3 ==========
-- Find all planets whose radius is greater than the average radius of all planets.
-- Use a scalar subquery.
SELECT id, name
FROM Planet
WHERE radius > (SELECT AVG(radius) FROM Planet);
