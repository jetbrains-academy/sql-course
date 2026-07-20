-- ========== Query 1 ==========
-- Show ALL planets together with the numbers of flights to them. Planets with no
-- flights must still appear, with an empty (NULL) flight number.
SELECT P.name AS planet, F.num
FROM Planet P LEFT JOIN Flight F ON P.id = F.planet_id;

-- ========== Query 2 ==========
-- Show the names of the planets that have no flights at all.
SELECT P.name AS planet
FROM Planet P LEFT JOIN Flight F ON P.id = F.planet_id
WHERE F.num IS NULL;
