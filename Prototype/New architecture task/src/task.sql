-- ========== Query 1 ==========
SELECT LOWER(?) = 'the moon' AS first_result;

-- ========== Query 2 ==========
SELECT id, name from Planet where is_inhabited=FALSE and climate != 'cold';

-- ========== Query 3 ==========
SELECT id, name from Planet where is_inhabited=FALSE;