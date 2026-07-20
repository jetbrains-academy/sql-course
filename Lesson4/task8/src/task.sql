-- ========== Query 1 ==========
-- For every flight, show the destination planet name and the flight number.
SELECT P.name AS planet, F.num
FROM Planet P JOIN Flight F ON P.id = F.planet_id;

-- ========== Query 2 ==========
-- Show the dates of all flights to inhabited planets.
SELECT F.flight_date
FROM Planet P JOIN Flight F ON P.id = F.planet_id
WHERE P.is_inhabited;

-- ========== Query 3 ==========
-- For every flight, show the planet name, the flight number, and the spacecraft name.
SELECT P.name AS planet, F.num, S.name AS ship
FROM Planet P JOIN Flight F ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id;
