-- ========== Query 1 ==========
-- Find the largest number of flights any single spacecraft made in a single year.
-- Use a subquery in FROM that first counts flights per spacecraft per year.
SELECT MAX(flight_count) AS max_flights
FROM (
    SELECT spacecraft_id, strftime('%Y', flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, flight_year
);

-- ========== Query 2 ==========
-- Count how many (spacecraft, year) combinations had 4 or more flights.
-- Use a subquery in FROM and filter on the counted value.
SELECT COUNT(*) AS busy_groups
FROM (
    SELECT spacecraft_id, strftime('%Y', flight_date) AS y, COUNT(*) AS c
    FROM Flight
    GROUP BY spacecraft_id, y
)
WHERE c >= 4;
