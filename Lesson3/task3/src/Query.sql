-- ========== Query 1 ==========
-- Find the planet where `id` is 42
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE true;

-- ========== Query 2 ==========
-- Find all planets where the value of "climate" attribute is
-- either "hot" or "warm"
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE true;

-- ========== Query 3 ==========
-- Find all planets where the value of "climate" attribute is
-- anything but "hot", "warm", "extremely hot", "cold" and "extremely cold"
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE true;

-- ========== Query 4 ==========
-- Find all certainly inhabited planets where the value of "radius" attribute is
-- greater or equal to 5000 and is less or equal to 7500
SELECT id, name, radius
FROM Planet
WHERE true;

-- ========== Query 5 ==========
-- Find all planets that meet all of the following criteria:
--   it is inhabited
--   its climate is "mild"
--   its radius is less than 8000
--   it is not militarized
SELECT id, name, is_inhabited, climate, has_weapons
FROM Planet
WHERE true;
