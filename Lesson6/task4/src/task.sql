-- ========== Query 1 ==========
-- Using a CTE, find the (spacecraft, year) with the most flights.
-- The CTE T counts flights per spacecraft per year; the main query keeps the row(s)
-- whose flight_count equals the maximum over T.
WITH T AS (
    SELECT spacecraft_id, strftime('%Y', flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, flight_year
)
SELECT spacecraft_id, flight_year, flight_count
FROM T
WHERE flight_count = (SELECT MAX(flight_count) FROM T);

-- ========== Query 2 ==========
-- Using a CTE that counts each spacecraft's total flights (across all years),
-- list the spacecraft that made more than 5 flights in total.
WITH per_spacecraft AS (
    SELECT spacecraft_id, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id
)
SELECT spacecraft_id, flight_count
FROM per_spacecraft
WHERE flight_count > 5;
