-- Try running these against L3_planet.sqlite, following the steps in this task.
-- Put the caret in a statement and press Ctrl+Enter (or click the green Run icon).

-- All planets and their climate
SELECT id, name, climate
FROM Planet;

-- Only the inhabited planets, largest first
SELECT name, radius
FROM Planet
WHERE is_inhabited = 1
ORDER BY radius DESC;
