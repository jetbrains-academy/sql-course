Remember the query we wrote to find the _arg max_ – a spacecraft that made the highest number of flights? 

```sql
SELECT * 
FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)
) AS T
WHERE flight_count = (SELECT MAX(flight_count) FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
)) AND flight_year = 2121
```

We had to write the same counting subquery twice: first to calculate the maximum value, and then to retrieve the row corresponding to that maximum. 
This approach naturally raises two main concerns. First, should we need to modify the counting logic, we have to update it in two separate places.
Second, is it efficient? Will the engine execute the subquery just once, or evaluate it twice?

In modern SQL, Common Table Expressions (CTEs) offer a clean solution by allowing you to define a table expression once and reference it across different parts
of a query. You can think of a CTE as a function that returns a table and potentially caches its results.

Essentially, a CTE is a subquery with an alias defined 
before the main query, which can then be referenced by its alias anywhere in that main statement. 
The syntax for defining a CTE is slightly more explicit than standard subquery syntax: 

```sql
-- Here goes a CTE, which can be referenced as T in subsequent queries
WITH T AS (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
)
-- Here goes the main query
SELECT * FROM T WHERE flight_count = (SELECT MAX(flight_count) FROM T)
```


We defined the counting subquery as a CTE only once, eliminating code duplication. 
Will it be executed only once as well? The answer is: it depends. 
It depends on the database engine and its particular version; it also depends on how many times the CTE is referenced in the query, 
and probably on other factors. Some database engines always save the output of a CTE to disk, while others recalculate
the CTE every time it is referenced. Each approach can result in performance issues or performance gains.

We can also define multiple Common Table Expressions by separating them with commas. Logically, they are evaluated in the
order they are declared, meaning later CTEs can reference the results of earlier ones. For instance,
if you need to calculate subtotals alongside a grand total, you can define a series of CTEs that aggregate 
data across progressively smaller sets of fields:


```sql
-- This CTE counts flights grouped by spacecraft and year.
WITH T AS (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
),
-- This CTE calculates flight subtotals grouped by spacecraft only.
S AS (
    SELECT spacecraft_id, SUM(flight_count) AS total_flight_count FROM T GROUP BY spacecraft_id
),
-- This CTE calculates the grand total number of flights.
R AS (SELECT SUM(total_flight_count) AS grand_total FROM S)
-- Here starts the main query. It joins the CTEs so each row includes
-- the spacecraft ID, flight year, annual flight count, and total flights 
-- for that spacecraft.
SELECT * FROM T JOIN S USING(spacecraft_id) CROSS JOIN R
```

Common Table Expressions offer great power, and naturally, it comes with great responsibility. 
Without careful design, it is easy to slip into writing inefficient and difficult to understand SQL. Consider this example: 

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

This query simply searches for flights made by `Falcon 22` to planets with a mild climate, and it can easily be
rewritten as follows: 

```sql
SELECT flight_date
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id=S.id
              JOIN Planet P     ON F.planet_id=P.id 
WHERE P.climate='mild' AND S.name='Falcon 22'
```

The rewritten query is shorter, easier for an experienced SQL programmer to read, and significantly
more efficient. 
The latter query can execute several times faster than the former one – even on modern  
database engines like PostgreSQL 14 – while on older database versions, the performance difference can reach several orders of magnitude.

This is by no means to say that Common Table Expressions are inherently inefficient. 
They are highly effective when a query is written in an optimizer-friendly, declarative style that operates on set-based data _en masse_, 
rather than breaking logic into step-by-step instructions. 
While step-by-step decomposition may appeal to programmers trained in general-purpose procedural, object-oriented, 
or functional languages, it often creates "optimization fences" – boundaries that prevent the query 
optimizer from optimizing execution across the entire query.
