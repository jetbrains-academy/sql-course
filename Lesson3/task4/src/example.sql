-- Run these examples against L3_planet.sqlite to see how subqueries work in WHERE.

-- The IN operator with an explicit list
SELECT * FROM Planet
WHERE climate IN ('mild', 'warm');

-- The IN operator with a subquery: uninhabited planets whose climate
-- matches the climate of some inhabited planet
SELECT * FROM Planet
WHERE NOT is_inhabited
  AND climate IN (SELECT climate FROM Planet WHERE is_inhabited);

-- EXISTS with an uncorrelated subquery returns the same value for every row,
-- so this selects all rows
SELECT * FROM Planet
WHERE EXISTS (SELECT climate FROM Planet WHERE is_inhabited);

-- EXISTS with a correlated subquery: the same "matching climate" problem,
-- where the alias P refers to the row of the outer query
SELECT * FROM Planet AS P
WHERE NOT is_inhabited
  AND EXISTS (SELECT id FROM Planet WHERE is_inhabited AND climate = P.climate);

-- Note: SQLite does not support the ANY / SOME / ALL operators shown in the
-- theory, so those examples are omitted here.
