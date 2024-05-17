-- ========== Query 1 ==========
-- In the query below, edit the boolean expression so that instead of checking that
-- the value of the placeholder {0} IS NOT an empty string it checked that it
-- EQUALS to string 'The moon' regardless of the case.
-- Make sure to keep the placeholder in the solution.
SELECT LOWER('{0}') = 'the moon' AS first_result;

-- ========== Query 2 ==========
-- In the query below, edit the boolean expression so that instead of checking that
-- the value of the placeholder {0} is greater than zero it checked that the value
-- is a square of any integer number from 2 to 10 inclusive. Hint: use `IN` operator.
-- Make sure to keep the placeholder in the solution.
SELECT SQRT({0}) IN (2,3,4,5,6,7,8,9,10) AS second_result;
