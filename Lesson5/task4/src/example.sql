-- Run these against astrofleet.sqlite to see GROUP BY in action.

-- Max radius and planet count per climate, inhabited planets only
SELECT climate, MAX(radius), COUNT(*) FROM Planet WHERE is_inhabited GROUP BY climate;

-- Count flights per planet, and per spacecraft
SELECT planet_id, COUNT(id) FROM Flight GROUP BY planet_id;
SELECT spacecraft_id, COUNT(id) FROM Flight GROUP BY spacecraft_id;

-- Group by two columns: flights per (planet, spacecraft)
SELECT planet_id, spacecraft_id, COUNT(id) FROM Flight GROUP BY planet_id, spacecraft_id;

-- Group by an expression
SELECT radius >= 5000, COUNT(*) FROM Planet GROUP BY radius >= 5000;

-- Count flights per spacecraft INCLUDING those with none (Comet 9 shows 0)
SELECT S.id, S.name, COUNT(F.id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id = F.spacecraft_id
GROUP BY S.id, S.name;
