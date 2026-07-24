-- Run these against L5_astrofleet.sqlite to see selective aggregates and group filters.

-- Selective aggregates with FILTER (WHERE ...)
SELECT P.id,
       COUNT(*)                                AS total_flights,
       COUNT(*) FILTER (WHERE S.capacity > 5)  AS big_capacity_flights,
       COUNT(*) FILTER (WHERE S.capacity <= 5) AS small_capacity_flights
FROM Flight F JOIN Spacecraft S ON S.id = F.spacecraft_id
              JOIN Planet P     ON P.id = F.planet_id
WHERE P.climate = 'mild'
GROUP BY P.id;

-- The same, using CASE (portable across engines)
SELECT P.id,
       COUNT(*)                                    AS total_flights,
       COUNT(CASE WHEN S.capacity > 5 THEN 1 END)  AS big_capacity_flights
FROM Flight F JOIN Spacecraft S ON S.id = F.spacecraft_id
              JOIN Planet P     ON P.id = F.planet_id
WHERE P.climate = 'mild'
GROUP BY P.id;

-- HAVING: keep only mild-climate planets with 3 or more flights
SELECT P.id, P.name, COUNT(*)
FROM Flight F JOIN Spacecraft S ON S.id = F.spacecraft_id
              JOIN Planet P     ON P.id = F.planet_id
WHERE P.climate = 'mild'
GROUP BY P.id, P.name
HAVING COUNT(*) >= 3;
