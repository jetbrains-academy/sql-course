-- Insert the parentheses properly to get 42 in the result
SELECT (8*10 + 4)/2 AS result

-- Add string concatenation and casing operators to get 'Hello, SQL' in the result
SELECT 'Hello, ' || UPPER('sql') AS result

-- In the query below, edit the boolean expression so that instead of checking that
-- the value of the placeholder {0} is greater than zero it checked that it
-- is greater or equal to 1.1 and less or equal to the square root of 122.
-- Make sure to keep the placeholder in the solution.
SELECT {0} BETWEEN 1.1 AND SQRT(122) AS result

-- In the query below, edit the boolean expression so that instead of checking that
-- the value of the placeholder {0} is not an empty string it checked that it
-- equals to string 'The moon' regardless of the case.
-- Make sure to keep the placeholder in the solution.
SELECT LOWER('{0}') = 'the moon' AS result

-- In the query below, edit the boolean expression so that instead of checking that
-- the value of the placeholder {0} is greater than zero it checked that the value
-- is a square of any integer number from 2 to 10 inclusive. Hint: use `IN` operator.
-- Make sure to keep the placeholder in the solution.
SELECT SQRT({0}) IN (2,3,4,5,6,7,8,9,10) AS result

