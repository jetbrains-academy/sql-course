-- ========== Query 1 ==========
-- Without using a JOIN operator, show the planet name and flight number for every
-- flight. Use a comma (cross join) in FROM and put the join condition in WHERE.
SELECT P.name AS planet, F.num
FROM Planet P, Flight F
WHERE P.id = F.planet_id;

-- ========== Query 2 ==========
-- Without using a JOIN operator, show the dates of all flights to inhabited planets.
-- Use the IN operator with a subquery.
SELECT flight_date
FROM Flight
WHERE planet_id IN (SELECT id FROM Planet WHERE is_inhabited);
