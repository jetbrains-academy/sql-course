-- ========== Query 1 ==========
-- Find the planet whose `id` is 42.
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE id = 42;

-- ========== Query 2 ==========
-- Find all planets whose `climate` is either "hot" or "warm".
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE climate IN ('hot', 'warm');

-- ========== Query 3 ==========
-- Find all planets whose `climate` is anything but
-- "hot", "warm", "extremely hot", "cold" and "extremely cold".
SELECT id, name, is_inhabited, climate
FROM Planet
WHERE climate NOT IN ('hot', 'warm', 'extremely hot', 'cold', 'extremely cold');

-- ========== Query 4 ==========
-- Find all certainly inhabited planets whose `radius` is
-- greater or equal to 5000 and less or equal to 7500.
SELECT id, name, radius
FROM Planet
WHERE is_inhabited AND radius BETWEEN 5000 AND 7500;

-- ========== Query 5 ==========
-- Find all planets that meet all of the following criteria:
--   it is inhabited,
--   its climate is "mild",
--   its radius is less than 8000,
--   it is not militarized (has no weapons).
SELECT id, name, is_inhabited, climate, has_weapons
FROM Planet
WHERE is_inhabited AND climate = 'mild' AND radius < 8000 AND NOT has_weapons;
