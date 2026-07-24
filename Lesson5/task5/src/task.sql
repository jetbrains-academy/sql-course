-- ========== Query 1 ==========
-- For each climate, find the maximum planet radius.
SELECT climate, MAX(radius) AS max_radius
FROM Planet
GROUP BY climate;

-- ========== Query 2 ==========
-- For every spacecraft, count how many flights it made, INCLUDING spacecraft
-- with no flights (they must show 0). Output the spacecraft id and the count.
SELECT S.id, COUNT(F.id) AS flights
FROM Spacecraft S LEFT JOIN Flight F ON S.id = F.spacecraft_id
GROUP BY S.id;

-- ========== Query 3 ==========
-- Count how many times each spacecraft flew to each planet.
-- Output planet_id, spacecraft_id, and the count.
SELECT planet_id, spacecraft_id, COUNT(*) AS flights
FROM Flight
GROUP BY planet_id, spacecraft_id;
