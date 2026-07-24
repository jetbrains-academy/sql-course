-- Run these against L6_astrofleet.sqlite to see common table expressions (CTEs).

-- Arg-max via a CTE: the (spacecraft, year) with the most flights.
-- T is defined once and referred to twice.
WITH T AS (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, flight_year
)
SELECT * FROM T WHERE flight_count = (SELECT MAX(flight_count) FROM T);

-- Several CTEs evaluated in order: per-(spacecraft,year) counts, per-spacecraft
-- subtotals, and the grand total (a single row, joined in with CROSS JOIN).
WITH T AS (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, flight_year
),
S AS (
    SELECT spacecraft_id, SUM(flight_count) AS total_flight_count FROM T GROUP BY spacecraft_id
),
R AS (SELECT SUM(total_flight_count) AS grand_total FROM S)
SELECT * FROM T JOIN S USING(spacecraft_id) CROSS JOIN R;

-- A step-by-step CTE chain is often clearer as one join: flights by Falcon 22 to mild planets
SELECT flight_date
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id = S.id
              JOIN Planet P     ON F.planet_id = P.id
WHERE P.climate = 'mild' AND S.name = 'Falcon 22';
