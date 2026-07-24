-- Run these against astrofleet.sqlite to see subqueries in the FROM clause.

-- Spacecraft with two flights in the same year but different months, one to an
-- inhabited planet and one to an uninhabited one (the subquery T is a table expression)
SELECT DISTINCT F.spacecraft_id
FROM Flight F JOIN Planet P ON P.id = F.planet_id
JOIN (
    SELECT spacecraft_id, flight_date
    FROM Flight F2 JOIN Planet P2 ON P2.id = F2.planet_id
    WHERE P2.is_inhabited
) AS T ON F.spacecraft_id = T.spacecraft_id
      AND CAST(strftime('%Y', T.flight_date) AS INTEGER) = CAST(strftime('%Y', F.flight_date) AS INTEGER)
      AND CAST(strftime('%m', T.flight_date) AS INTEGER) <> CAST(strftime('%m', F.flight_date) AS INTEGER)
WHERE NOT P.is_inhabited;

-- Flights per spacecraft per year
SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
FROM Flight
GROUP BY spacecraft_id, flight_year;

-- The greatest flight count in a given year, using the query above as a subquery in FROM
SELECT MAX(flight_count) AS max_flight_count
FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, flight_year
) AS T
WHERE flight_year = 2121;
