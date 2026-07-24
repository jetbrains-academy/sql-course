-- Run these against astrofleet.sqlite to see how outer joins work.

-- LEFT OUTER JOIN: all planets, with NULLs where there are no flights
SELECT * FROM Planet LEFT OUTER JOIN Flight ON id = planet_id;

-- Swapping the operands: here it equals the inner join, since every flight has a planet
SELECT * FROM Flight LEFT OUTER JOIN Planet ON id = planet_id;

-- RIGHT OUTER JOIN: keeps all planets on the right side
SELECT * FROM Flight RIGHT OUTER JOIN Planet ON id = planet_id;

-- FULL OUTER JOIN: keeps unmatched rows from both sides
SELECT * FROM Planet FULL OUTER JOIN Flight ON id = planet_id;
