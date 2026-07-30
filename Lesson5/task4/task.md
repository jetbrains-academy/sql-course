Remember how we found the maximum radius of the planets with a mild climate?

```sql
-- This will find the maximum radius across the planets with a mild climate
SELECT MAX(radius) FROM Planet WHERE climate = 'mild';
```

What if we want to find the maximum radius value for each climate, no matter how many distinct climate values there are?
At this point, people who know the concept of loops in general-purpose programming languages start thinking about 
something like this:

```
-- This is a pseudocode
for $climate in "SELECT climate FROM Planet":
    SELECT MAX(radius) FROM Planet WHERE climate=$climate
```

Although it is technically possible to emulate such loops in an SQL query, and surely possible in the application code, 
in SQL we solve such problems in a different way.

Let's talk about the `GROUP BY` clause. It is a keyword `GROUP BY` followed by one or more comma-separated expressions. 
Lexically it follows the `WHERE` clause, if there is one in a query, or the `FROM` clause if there is no `WHERE`. 
In the majority of cases, the expressions in `GROUP BY` are just column names, although technically any expression is allowed. 
For now, we will consider the case with just one column name expression and then generalize the idea. 
We will call the column that is used in `GROUP BY` a _grouping column_.

So, let's see what logically happens if we write something like this:

```sql
SELECT <select_list> -- <<== we will see later what we can select here   
FROM Planet WHERE is_inhabited
GROUP BY climate
```

First, the database engine builds the output of `FROM-WHERE` as usual, which in this particular case leaves 
only the inhabited planets, and then feeds the output to `GROUP BY`. Grouping evaluates the
expression, which in this particular case is just the value of the grouping column `climate`, for every row. 
The rows with the same value of `climate` go into the same group. 
When scanning of the whole output is complete, we get a few groups – one for each value of `climate`.
The groups do not intersect with each other, that is, they partition the output of `FROM-WHERE`.

The subsequent evaluation of the `SELECT` clause will output exactly one row per group. For each group, we can select the value
of the grouping column and the values of aggregate functions, which now build their input 
from the rows constituting the group. We can't select anything else. Let's look at some valid and invalid queries:

```sql
-- This query is OK. The select list is a single aggregate function.
SELECT MAX(radius)  
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is OK. The select list includes a grouping column and two aggregate functions. 
SELECT climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is not OK.
SELECT id, -- << == this is not a grouping column or an aggregate function 
       climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate
```

Why can't we select the value of a non-grouping or non-aggregated column? 
Well, the database engine must create exactly one row per group, as instructed by the standard, and the values in that row 
are supposed to be deterministic (unless we use non-deterministic functions in `SELECT`). 
The value of a grouping column is certainly the same for all rows from the same group, as well as the value of 
deterministic aggregate functions. 
The values of any non-grouping column may or may not be the same in all rows from the group. 
For a database engine, it is safe to assume that the values of non-grouping columns are different unless the engine 
can prove the opposite. The engine has no reasons to choose one of the different values in a non-grouping column 
in favor of others, so it just refuses to run such queries. 
It's worth noticing that there are engines that are more optimistic and allow for using non-grouping columns in 
`SELECT` from a grouped table, hoping that programmers won't shoot their own foot. 
When a non-grouping column is in the select list of a query with a `GROUP BY` clause, they choose a value from an essentially random row from the group. 

### Grouping by more than one column and grouping by expressions

We can use more than one grouping column in a `GROUP BY` clause, separating them with commas. 
In this case, all rows in the same group will have the same values in the same grouping columns. 
Let's look at the `Flight` table. Depending on how we group, we count different things:

```sql
-- This counts the flights to each planet.
SELECT planet_id, COUNT(id) FROM Flight
GROUP BY planet_id

-- This counts the flights made by each spacecraft.
SELECT spacecraft_id, COUNT(id) FROM Flight
GROUP BY spacecraft_id
```

Let's say that we want to count how many times each spacecraft flew to each planet. We need to group the flight rows by 
a pair of columns – `planet_id, spacecraft_id` – and count them:

```sql
-- This counts the flights for each planet-and-spacecraft pair
SELECT planet_id, spacecraft_id, COUNT(id) FROM Flight
GROUP BY planet_id, spacecraft_id
```

One more useful case when grouping by many columns makes sense is discussed below in the _Tips and tricks_ section.

Besides column names, we can use any expressions in the `GROUP BY` clause. The rules of grouping remain the same: 
all rows in one group have the same value of the grouping expression. For instance, we can partition our planets into
groups of those with a radius greater than or equal to 5000 and the rest, and count them: 

```sql
-- This counts the planets in each radius group
SELECT radius >= 5000, COUNT(*) FROM Planet
GROUP BY radius >= 5000
```


### Tips and tricks

Let's again take a look at one of the queries above:

```sql
-- This counts the flights made by each spacecraft
SELECT spacecraft_id, COUNT(id) AS flight_count FROM Flight
GROUP BY spacecraft_id
```

If we look closely, we'll see that actually it won't do what it promises in the comment. 
It counts the flights made by those spacecraft that had at least one flight. 
If some spacecraft had no flights, it will be missing in the output. 
What if we want to output such spacecraft as well, with 0 in the `flight_count` column? 
There are many ways of doing this, but one of the canonical and most common ways is using the outer join with the spacecraft table. 
Spacecraft with no flights will be in the output of the outer join `Spacecraft LEFT JOIN Flight` with `NULL` values in `Flight` columns. 
Now we have to pay attention to how we group and what we count.
The following queries are all supposed to count the number of flights made by each spacecraft, but some of them are not correct. 
Read the comments to learn why: 

```sql
-- This query looks good, but in fact it is not correct because the grouping column is Flight.spacecraft_id, which is 
-- NULL for spacecraft with no flights
SELECT F.spacecraft_id, COUNT(*) 
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY F.spacecraft_id


-- This query looks better, but it is not correct either because we use COUNT(*), which is never 0, as there are no groups with 
-- zero rows.
SELECT S.id, COUNT(*)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- This query is correct because COUNT(F.spacecraft_id) will ignore NULL values and return 0 for the spacecraft
-- with no flights. Besides, F.spacecraft_id is NULL **only** in the outer part of the join, so it will properly
-- count the spacecraft with flights.
SELECT S.id, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- Let's imagine that there is a nullable column "cargo_id" in the Flight table. Its value is NULL if there was no 
-- cargo on the flight, and not NULL otherwise. The query below is not correct because COUNT will skip the flights with no cargo.
-- It will return 0 for spacecraft with no flights, but it may return a wrong value and even 0 for the spacecraft that
-- did perform flights but did not carry any cargo.
SELECT S.id, COUNT(F.cargo_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

What if we need to output the names of spacecraft along with their ids? A naive solution will not work because `name` is
not a grouping column:

```sql
-- This will not work on most database engines because name is not a grouping column or an aggregate function.
SELECT S.id, S.name, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

However, this is the case when we are certain that the values of the `Spacecraft.name` column are the same in all rows 
within the same group. 
We are certain because we know that if two spacecraft rows have the same value of `id`, then they have the same `name` value. 
The `Spacecraft.id` column is a grouping column, and this means that all rows within the same group have the same id and thus the same name.
This knowledge allows for a couple of tricks.

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

Can we just always add a non-grouping column into the `GROUP BY` clause? 
For instance, if we want to fix the following query, can we append `id` to `climate` in `GROUP BY`? 
Let's forget why one would write such a query – there might be many reasons – but just imagine that our goal is 
to make the database engine happy.

```sql
-- This query is not OK, and the database engine will report an error. 
SELECT id, -- << == this is not a grouping column or an aggregate function
climate, MAX(radius)
FROM Planet WHERE is_inhabited
GROUP BY climate
```

If we add `id` to the grouping columns – `GROUP BY climate, id` – the database engine will be happy, 
but most likely it will output not what we expect.
Since `id` is an identifier and there are no planets with the same value of `id`, adding `id` to the grouping columns
makes all groups comprise just one row. Such query is basically equivalent to `SELECT * FROM Planet`.

Chances are that the goal was to find the id of a planet with the maximum radius for each climate, and such a task 
can be solved using subqueries in `FROM`, which we will learn in the next lesson. 