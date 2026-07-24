-- ========== Query 1 ==========
-- Find the planets that have 4 or more flights. Output the planet_id and the
-- flight count, keeping only the groups with at least 4 flights.
SELECT planet_id, COUNT(*) AS flights
FROM Flight
GROUP BY planet_id
HAVING COUNT(*) >= 4;

-- ========== Query 2 ==========
-- For each planet, count the total number of flights to it and, separately, the
-- number of those flights made by a large spacecraft (capacity greater than 5).
SELECT P.id, COUNT(*) AS total, COUNT(*) FILTER (WHERE S.capacity > 5) AS big
FROM Flight F JOIN Spacecraft S ON S.id = F.spacecraft_id
              JOIN Planet P ON P.id = F.planet_id
GROUP BY P.id;
