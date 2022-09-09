## Common Table Expressions

Remember the query which we wrote to find an arg max, a spacecraft which did the greatest count of flights? 

```sql
SELECT * 
FROM (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)
) AS T
WHERE flight_count = (SELECT MAX(flight_count) FROM (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)    
)) AND flight_year = 2084
```

We had to write the same counting subquery twice to find the maximum and then to find the row with the maximum value. 
This naturally raises concerns. First, shall we wish to modify the counting subquery, we have to do it twice.
Second, will it be efficient? Will the engine execute the counting subquery just once or twice?

In the modern SQL, there is a good way to write a table expression just once and refer to it from different parts
of a query. You may think of it as a function that returns a table and, probably, caches the result of calculations.

### Common Table Expressions

So, a common table expression, abbreviated as CTE, is essentially a subquery with an alias, which lexically 
goes before the "main" query and can be referred by the alias in the main query. 
The syntax of defining a CTE is slightly more verbose than a subquery syntax: 

```sql
-- Here goes a CTE, which can be referred as T in subsequent queries
WITH T AS (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)    
)
-- Here goes the main query
SELECT * FROM T WHERE flight_count = (SELECT MAX(flight_count) FROM T)
```


We defined the counting subquery only once as CTE, and removed code duplication. 
Will it be executed just once as well? The answer is "it depends". 
It depends on the database engine and its particular version, it depends on how many times a CTE is referred in a query 
and probably on other factors. Some database engines would always persist CTE output to the disk, some would calculate
them every time and both the former and the later strategies may cause both performance issues and performance gains.

We can define more than one common table expressions, delimiting them with comma. Logically they are evaluated in lexical
order, and CTEs which are declared later in the lexical order can use the output of CTEs declared earlier. For instance,
we can define two CTEs that calculate aggregated values grouping by reducing set of fields:


```sql
-- This CTE counts flights grouping by a spacecraft and year.
WITH T AS (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)    
),
-- This CTE aggregates the counts calculated by the previous CTE, grouping by spacecraft only.
S AS (
    SELECT spacecraft_id, SUM(flight_count) AS total_flight_count FROM T GROUP BY spacecraft_id
)
-- Here starts the main query. It joins the outputs of two CTEs, so that in the result in every row we
-- get a spacecraft id, year, the count of flights of the spacecraft in that year and the total count of flights
-- made by the spacecraft.
SELECT * FROM T JOIN S USING(spacecraft_id)
```

Common table expressions is a great power, and naturally it comes with great responsibility. 
With CTEs it is easy to start writing bad SQL code, inefficient and difficult to understand. Consider this example: 

```sql
WITH A AS (
    SELECT Flight.*, Spacecraft.name AS spacecraft_name, Planet.name AS planet_name, Planet.climate
    FROM Flight, Spacecraft, Planet
),
B AS (
    SELECT * FROM A WHERE climate = 'mild'  
),
C AS (
    SELECT flight_date FROM B WHERE spaceraft_name = 'Falcon 9'
)
SELECT * FROM C
```

This query just searches for flights made by `Falcon 9` to the planets with mild climate, and it can be easily 
rewritten as follows: 

```sql
SELECT flight_date
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id=S.id
              JOIN Planet P     ON F.planet_id=P.id 
WHERE P.climate='mild' AND S.name='Falcon 9'
```

The rewritten query is shorter, is actually easier to understand for a relatively experienced SQL programmer and finally,
it is just more efficient. 
The latter query may execute a few times faster than the former even when running on pretty modern versions 
of advanced database engines, such as PostgreSQL 14, while for older versions the difference may reach a few orders of magnitude.

This is by no means to say that common table expressions are inefficient. 
They are efficient if a query is written in optimizer-friendly declarative way and processes many tables en masse, 
rather than being decomposed into "step-by-step instructions". 
Such decompositions may look good to the eyes of programmers who use general purpose procedural, object-oriented 
and even functional languages, but in fact they create so-called "optimization fences", that is, obstacles which a query 
optimizer can't easily overcome.