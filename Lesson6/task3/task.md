Remember the query we wrote to find an arg max – a spacecraft that had the greatest count of flights? 

```sql
SELECT * 
FROM (
    SELECT F.spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)
) AS T
WHERE flight_count = (SELECT MAX(flight_count) FROM (
    SELECT F.spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
)) AND flight_year = 2121
```

We had to write the same counting subquery twice to find the maximum and then to find the row with the maximum value. 
This naturally raises concerns. First, shall we wish to modify the counting subquery, we have to do it twice.
Second, will it be efficient? Will the engine execute the counting subquery just once or twice?

In the modern SQL, there is a good way to write a table expression just once and refer to it from different parts
of a query. You may think of it as a function that returns a table and, probably, caches the result of calculations.

So, a common table expression, abbreviated as CTE, is essentially a subquery with an alias, which lexically 
goes before the "main" query and can be referred to by the alias in the main query. 
The syntax of defining a CTE is slightly more verbose than the subquery syntax: 

```sql
-- Here goes a CTE, which can be referred to as T in subsequent queries
WITH T AS (
    SELECT F.spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
)
-- Here goes the main query
SELECT * FROM T WHERE flight_count = (SELECT MAX(flight_count) FROM T)
```


We defined the counting subquery as CTE only once and removed code duplication. 
Will it be executed just once as well? The answer is "it depends". 
It depends on the database engine and its particular version, it also depends on how many times the CTE is referred to in a query, 
and probably on other factors. Some database engines would always save output of each CTE to the disk, some would recalculate
CTE every time it is used, and both the former and the latter strategies may result in both performance issues and performance gains.

We can define more than one common table expression, delimiting them with a comma. Logically, they are evaluated in lexical
order, and CTEs that are declared later in the lexical order can use the output of CTEs declared earlier. For instance,
if we need to calculate subtotals and grand total, we can define a few CTEs that calculate aggregated values grouping 
data by sequentially contracting set of fields:


```sql
-- This CTE counts flights grouping by the spacecraft and year.
WITH T AS (
    SELECT F.spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
),
-- This CTE calculates flight subtotals grouping by spacecraft only.
S AS (
    SELECT spacecraft_id, SUM(flight_count) AS total_flight_count FROM T GROUP BY spacecraft_id
),
-- This CTE calculates the grand total value of flights.
R AS (SELECT SUM(total_flight_count) AS grand_total FROM S)
-- Here starts the main query. It joins the outputs of the two CTEs so that in the result, in every row we
-- get a spacecraft id, year, the count of flights of the spacecraft in that year and the total count of flights
-- made by the spacecraft.
SELECT * FROM T JOIN S USING(spacecraft_id) CROSS JOIN R
```

Common table expressions have a great power, and naturally, it comes with great responsibility. 
With CTEs, it is easy to start writing bad SQL code – inefficient and difficult to understand. Consider this example: 

```sql
WITH A AS (
    SELECT Flight.*, Spacecraft.name AS spacecraft_name, Planet.name AS planet_name, Planet.climate
    FROM Flight, Spacecraft, Planet
),
B AS (
    SELECT * FROM A WHERE climate = 'mild'  
),
C AS (
    SELECT flight_date FROM B WHERE spacecraft_name = 'Falcon 22'
)
SELECT * FROM C
```

This query just searches for flights made by `Falcon 22` to the planets with a mild climate, and it can be easily 
rewritten as follows: 

```sql
SELECT flight_date
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id=S.id
              JOIN Planet P     ON F.planet_id=P.id 
WHERE P.climate='mild' AND S.name='Falcon 22'
```

The rewritten query is shorter, it is actually easier to understand for a relatively experienced SQL programmer, and finally,
it is just more efficient. 
The latter query may execute a few times faster than the former one even when running on pretty modern versions 
of advanced database engines, such as PostgreSQL 14, while for older versions, the difference may reach a few orders of magnitude.

This is by no means to say that common table expressions are inefficient. 
They are efficient if a query is written in an optimizer-friendly declarative way and processes many tables en masse, 
rather than being decomposed into "step-by-step instructions". 
Such decompositions may look good to the eyes of programmers who use general purpose procedural, object-oriented, 
and even functional languages, but in fact, they create so-called "optimization fences", that is, obstacles a query 
optimizer can't easily overcome.