-- Run these against marsoflot.sqlite to see how INNER JOIN works.

-- Inner join: every planet paired with the flights to it
SELECT * FROM Planet JOIN Flight ON Planet.id = Flight.planet_id;

-- INNER is optional, and inner join is commutative (same result if you swap the tables)
SELECT * FROM Flight JOIN Planet ON Planet.id = Flight.planet_id;

-- The join condition can be any boolean expression: for each flight, all earlier flights
SELECT * FROM Flight AS F1 JOIN Flight AS F2 ON F1.flight_date > F2.flight_date;

-- Joining a table with itself: planets with the same climate and a smaller radius
SELECT * FROM Planet P1 JOIN Planet P2 ON P1.climate = P2.climate AND P1.radius > P2.radius;
