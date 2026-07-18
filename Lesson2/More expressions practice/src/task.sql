-- ========== Query 1 ==========
-- Make it true only when ? equals 'The moon', ignoring case. Keep the ? placeholder.
SELECT LOWER(?) = 'the moon' AS first_result;

-- ========== Query 2 ==========
-- Make it true only when ? is a square of an integer from 2 to 10. Keep the ? placeholder.
SELECT SQRT(?) IN (2,3,4,5,6,7,8,9,10) AS second_result;
