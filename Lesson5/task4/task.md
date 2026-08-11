Remember how we found the maximum radius among planets with a mild climate?

```sql
-- This finds the maximum radius across planets with a mild climate
SELECT MAX(radius) FROM Planet WHERE climate = 'mild';
```

What if we want to find the maximum radius for each climate type, no matter how many distinct climates exist?
People familiar with the concept of loops in general-purpose programming languages might instinctively think of 
something like this:

```
-- This is pseudocode
for $climate in "SELECT climate FROM Planet":
    SELECT MAX(radius) FROM Planet WHERE climate=$climate
```

While you could theoretically emulate loops in application code, 
SQL handles this differently using the `GROUP BY` clause.

The `GROUP BY` clause takes one or more comma-separated expressions and 
lexically follows the `WHERE` clause (or the `FROM` clause if no `WHERE` condition exists. 
In most cases, these expressions are column names, although technically any expression is allowed. 
For now, we will consider the case with just one column name expression and then generalize the idea. 
We will call a column used in `GROUP BY` a _grouping column_.

Let's look at what logically happens when we write a query like this:

```sql
SELECT <select_list> -- <<== we will see what we can select here shortly   
FROM Planet WHERE is_inhabited
GROUP BY climate
```

First, the database engine processes the `FROM` and `WHERE` clauses as usual, which, in this example, leaves 
only the inhabited planets, and passes the output to `GROUP BY`. Grouping then evaluates the grouping
expression (here, the `climate` column), for every row. 
Rows sharing the same `climate` value are placed into the same group. 
Once all input rows are scanned, we get distinct groups – one for each value of `climate`.
These groups do not intersect with each other, partitioning the output of the `FROM` and `WHERE` clauses.

Next, the `SELECT` clause evaluates to output exactly one row per group. For each group, we can select the
grouping column and aggregate functions (which now compute their inputs 
from the rows belonging to that specific group). We can't select anything else. Let's look at some valid and invalid query examples:

```sql
-- This query is OK. The select list contains a single aggregate function.
SELECT MAX(radius)  
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is OK. The select list contains a grouping column and two aggregate functions. 
SELECT climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is not OK.
SELECT id, -- << == this is neither a grouping column nor an aggregate function 
       climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate
```

Why can't we select the value of a non-grouping or non-aggregated column? 
The SQL standard requires the database engine to produce exactly one row per group, and the values in that row 
must be deterministic (unless non-deterministic functions are explicitly used in `SELECT`). 
Grouping columns have the exact same value across all rows within a group, and aggregate functions compute a 
single deterministic value for the group. 
In contrast, a non-grouping column could contain different values across rows in the same group. 
Unless the database engine can prove otherwise, it assumes values in non-grouping columns differ from row to row. 
Because the engine has no reason to pick one row's value over another,  
it rejects the query. 
It's worth noting that some engines are more permissive and allow non-grouping columns in 
`SELECT` from a grouped table, hoping that programmers won't shoot themselves in the foot. 
They arbitrarily pick a value from an essentially random row within the group. 

### Grouping by multiple columns and expressions

You can group by multiple columns by separating them with commas. 
In this scenario, all rows within the same group will share identical values across all grouping columns. 
Let's look at the `Flight` table. Depending on how we group, we end up counting different things:

```sql
-- This counts flights to each planet.
SELECT planet_id, COUNT(id) FROM Flight
GROUP BY planet_id

-- This counts flights made by each spacecraft.
SELECT spacecraft_id, COUNT(id) FROM Flight
GROUP BY spacecraft_id
```

Suppose we want to count how many times each spacecraft flew to each planet. We need to group the flight rows by 
both `planet_id, spacecraft_id`, then count the rows in each group:

```sql
-- This counts flights for each planet-and-spacecraft pair
SELECT planet_id, spacecraft_id, COUNT(id) FROM Flight
GROUP BY planet_id, spacecraft_id
```

Another scenario where grouping by multiple columns is useful is discussed below in the _Tips and tricks_ section.

In addition to column names, you can use expressions in the `GROUP BY` clause. The fundamental rule remains the same: 
all rows within a group evaluate to the exact same value for the grouping expression. For instance, we can divide our planets into
two groups – those with a radius of 5000 or greater and those below – and count the number of planets in each: 

```sql
-- This counts planets in each radius group
SELECT radius >= 5000, COUNT(*) FROM Planet
GROUP BY radius >= 5000
```


### Tips and tricks

Let's again take a look at one of the queries from earlier:

```sql
-- This counts flights made by each spacecraft
SELECT spacecraft_id, COUNT(id) AS flight_count FROM Flight
GROUP BY spacecraft_id
```

If we look closely, we'll see that it doesn't quite do what the comment promises. 
It only counts flights for spacecraft that have made at least one flight. 
If a spacecraft has no flights, it will be missing from the results. 
What if we want to include those spacecraft in our output as well, showing `0` in the `flight_count` column? 
There are several ways to accomplish this, but the canonical approach is using an outer join with the spacecraft table. 
Spacecraft with no recorded flights will appear in the result of `Spacecraft LEFT JOIN Flight` with `NULL` values across all `Flight` columns. 
At this point, we need to be careful about how we group and what we count.
The following queries all attempt to count the number of flights made by each spacecraft, but some contain subtle errors. 
Read the comments below to see why: 

```sql
-- This query looks good, but in fact, it is incorrect because the grouping column is Flight.spacecraft_id, which is 
-- NULL for spacecraft with no flights
SELECT F.spacecraft_id, COUNT(*) 
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY F.spacecraft_id


-- This query looks better, but it is incorrect either because we use COUNT(*), which is never 0, as there are no groups with 
-- zero rows.
SELECT S.id, COUNT(*)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- This query is correct because COUNT(F.spacecraft_id) ignores NULL values and return 0 for spacecraft
-- with no flights. Besides, F.spacecraft_id is NULL **only** in the outer part of the join, so it will properly
-- count spacecraft with flights.
SELECT S.id, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- Imagine a nullable column "cargo_id" in the Flight table. Its value is NULL if the flight 
-- carried no cargo, and non-NULL otherwise. The query below is incorrect because COUNT skips flights without cargo.
-- It correctly returns 0 for spacecraft with no flights, but it returns an incorrect count (or even 0) for spacecraft that
-- performed flights without carrying cargo.
SELECT S.id, COUNT(F.cargo_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

What if we need to output the names of spacecraft along with their IDs? A naive approach fails because `name` is
not a grouping column:

```sql
-- This fails on most database engines because name is neither a grouping column nor wrapped in an aggregate function.
SELECT S.id, S.name, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

In this case, however, when we know for certain that `Spacecraft.name` has the exact same value across all rows 
within a given group. 
We know it because if two rows share the same `id` value, they naturally share the same `name`. 
Since `Spacecraft.id` is our grouping column, every row in a group has the same `id` and, consequently, the same `name`.
Understanding this concept enables a few common workaround techniques.

```sql
-- This works: we added name to the grouping columns.
SELECT S.id, S.name, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id, S.name

-- This works as well: we applied an aggregate function to the name column. 
-- Since we know that all names in the group are the same, the maximum value will also be the same.
SELECT S.id, MAX(S.name), COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

Can we simply add every non-grouping column to the `GROUP BY` clause? 
For instance, if we want to fix the following query, can we just add `id` alongside `climate` in `GROUP BY`? 
Setting aside why someone would write such a query – there might be many reasons – let's focus purely on 
how to make the database engine happy.

```sql
-- This query is not OK, and the database engine will report an error. 
SELECT id, -- << == this is neither a grouping column nor an aggregate function
climate, MAX(radius)
FROM Planet WHERE is_inhabited
GROUP BY climate
```

If we add `id` to the grouping columns – `GROUP BY climate, id` – the database engine will run the query without errors, 
but it will most likely produce not what we expect.
Since `id` is a unique identifier, no two planets share the same `id` value. Adding `id` to the grouping columns
causes every group to contain exactly one row, making the query equivalent to `SELECT * FROM Planet`.

If the goal was instead to find the `id` of the planet with the maximum radius for each climate, that task 
can be solved using subqueries in the `FROM` clause, which we will cover in the next lesson. 
