-- Run these against marsoflot.sqlite to see how join chains and filters work.

-- Inner join chain: planet + flight + spacecraft
SELECT *
FROM Planet P JOIN Flight F     ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id;

-- Outer join chain: keep all planets, even those with no flights
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
              LEFT JOIN Spacecraft S ON F.spacecraft_id = S.id;

-- Filter the joined result: flights performed by Falcon 25
SELECT P.name AS planet_name, F.flight_date, S.capacity
FROM Planet P JOIN Flight F     ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id
WHERE S.name = 'Falcon 25';
