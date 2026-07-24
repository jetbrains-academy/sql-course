-- Run these against astrofleet.sqlite to see joins expressed without the JOIN operator.

-- Cross join (Cartesian product) plus a filter equals an inner join
SELECT * FROM Planet CROSS JOIN Flight
WHERE Planet.id = Flight.planet_id;

-- A comma in FROM is the cross-join operator
SELECT * FROM Planet, Flight
WHERE Planet.id = Flight.planet_id;

-- A subquery with IN also expresses a join: dates of flights to uninhabited planets
SELECT flight_date FROM Flight
WHERE planet_id IN (SELECT id FROM Planet WHERE is_inhabited = FALSE);
