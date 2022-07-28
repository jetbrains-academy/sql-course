## Aggregate functions and rows grouping

Remember how did we find a maximum radius of planets with mild climate?

```sql
-- This will find the maximum radius across planets with mild climate
SELECT MAX(radius) FROM Planet WHERE climate = 'mild';
```

What if we want to find a maximum radius value for each climate, no matter how many distinct climate values are there?
At this point people who know the concept of loops in general-purpose programming languages start thinking about 
something like this:

```
-- This is a pseudocode
for $climate in "SELECT climate FROM Planet":
    SELECT MAX(radius) FROM Planet WHERE climate=$climate
```

Although it is technically possible to emulate such loops in SQL query, and surely possible in the application code, 
in SQL we solve such problems in a different way.

Let's talk about `GROUP BY` clause. It is a keyword `GROUP BY` followed by one or more comma-separated expressions, 
and lexically it follows the `WHERE` clause, if there is one in a query, or `FROM` clause if there is no `WHERE`. 
In the majority of cases those expressions are just column names, although any expression is allowed. For now, we 
will consider the case when there is just one expression which is a column name, and then generalize the idea. We will call 
the column which is used in `GROUP BY` a _grouping column_.

So, what happens if we write something like this:

```sql
SELECT [something] -- <<== intentionally unclear  
FROM Planet WHERE is_inhabited
GROUP BY climate
```

First, the database engine will build the output of `FROM-WHERE` as usual, which in this particular case will leave 
only inhabited planets, and then will feed the output to `GROUP BY`. Grouping will consider very row and evaluate the
expression, which in this particular case is just the value of the grouping column `climate`. 
The rows with the same value of `climate` go into the same group. 
When scanning of the whole output is complete, we get a few groups, one for each value of `climate`.
The groups do not intersect with each other, that is, they partition the output of `FROM-WHERE`.

The subsequent evaluation of `SELECT` clause will output exactly one row per group. In this group we can select the value
of the grouping column, and the values of aggregate functions, which now build their input 
separately for each group. We can't select anything else. Let's look at some valid and not valid queries:

```sql
-- This query is OK
SELECT MAX(radius)  
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is OK
SELECT climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate

-- This query is not OK
SELECT id, -- << == this is not a grouping column and not an aggregate function 
       climate, MAX(radius), COUNT(*)
FROM Planet WHERE is_inhabited
GROUP BY climate
```

Why we can't select the value of a non-grouping and non-aggregated column? Well, the database engine must create exactly one row 
per group, as instructed by the standard, and the values in that row are supposed to be deterministic 
(unless we use non-deterministic functions in `SELECT`). The value of a grouping column is certainly the same for all rows from the
same group, as well as the value of deterministic aggregate functions. The values of any non-grouping column may or may not
be the same in all rows from the group. For a database engine, it is safe to assume that the values of non-grouping columns
are different, unless the engine can prove the opposite. 
The engine has no reasons to choose one of the different values in a non-grouping column in favor of others, so it just 
refuses to run such queries. Worth noticing that there are engines which are more optimistic and which allow for using
non-grouping columns in `SELECT` from grouped table, hoping that programmers won't shoot their own foot. When a non-grouping
column is in a select list of a query with `GROUP BY` clause, they choose a value from essentially random row from a group. 

### Grouping by more than one column and grouping by expressions

We can use more than one grouping column in a `GROUP BY` clause, separating them with commas. 
In this case all rows in the same group will have the same values in the same grouping columns. 
Let's look at the `Flight` table. Depending on how we group, we can count different things:

```sql
-- This counts the flights to each planet
SELECT planet_id, COUNT(id) FROM Flight
GROUP BY planet_id

-- This counts the flights made by each spacecraft
SELECT spacecraft_id, COUNT(id) FROM Flight
GROUP BY spacecraft_id
```

Let's say that we want to count how many times each spacecraft flew to each planet. We need to group the flight rows by 
a pair of columns - `planet_id, spacecraft_id` -- and count them:

```sql
-- This counts the flights to each planet
SELECT planet_id, spacecraft_id, COUNT(id) FROM Flight
GROUP BY planet_id, spacecraft_id
```

One more useful case when grouping by many columns makes sense is discussed below in _Tips and tricks_ section.

Besides the column names, we can use any expressions in `GROUP BY` clause. The rules of grouping remain the same: 
all rows in one group have the same value of the grouping expression. For instance, how can partition our planets into
groups with radius greater or equal than 5000 and the rest, and count them: 

```sql
-- This counts the flights to each planet
SELECT radius >= 5000, COUNT(*) FROM Planet
GROUP BY radius >= 5000
```


### Tips and tricks

If we look at one of the queries above:

```sql
-- This counts the flights made by each spacecraft
SELECT spacecraft_id, COUNT(id) FROM Flight
GROUP BY spacecraft_id
```

It actually doesn't do what it promises in the comment. It counts the flights made by those spacecrafts that had at least
one flight. If some spacecraft made no flights, it will be missing. What if we want to output such spacecrafts as well,
with 0 in the flight count column? There are many ways of doing this, but one of the canonical and widely used ways is
using outer join with the all spacecraft table. Spacecrafts with no flights will be in the output of outer join 
`Spacecraft LEFT JOIN Flight` with `NULL` values in `Flight` columns. 
Now we have to pay attention on how we group and what we count.
Read the comments above the following queries, which are all supposed to count the number of flights made by each spacecraft: 

```sql
-- This query looks good, but in fact it is wrong, because the grouping column is Flight.spacecraft_id which is 
-- NULL for spacecrafts with no flights
SELECT S.id, COUNT(*) 
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY F.spacecraft_id


-- This query looks better, but it is wrong as well, because we count (*) which is never 0, as there are no groups with 
-- zero rows.
SELECT S.id, COUNT(*)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- This query is correct, because COUNT(F.spacecraft_id) will ignore NULL values and return 0 for the spacecrafts
-- with no flights. Besides, F.spacecraft_id is NULL **only** in the outer part of the join, so it will
-- count the spacecrafts with flights properly.
SELECT S.id, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id

-- Let's imagine that there is a nullable column "cargo_id" in the Flight table. Its value is NULL if there was no 
-- cargo on the flight, and not null otherwise. The query below is wrong, because COUNT will skip flights with no cargo.
-- It will return 0 for spacecrafts with no flights, but it may return wrong value and even 0 for spacecrafts which
-- did perform flights but did not carry any cargo.
SELECT S.id, COUNT(F.cargo_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

What if we need to output the names of spacecrafts along with their ids? A naive solution will not work because name is
not a grouping column:

```sql
-- This will not work on most database engines
SELECT S.id, S.name, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```

However, this is the case when we are certain that the values of `Spacecraft.name` column are the same in all rows 
within the same group. We are certain because `Spacecraft.id` column is the grouping column, and we know that if two 
spacecraft rows have the same value of id then they have the same name value. This knowledge allows for a couple of tricks.

```sql
-- This will work: we added name to the grouping columns
SELECT S.id, S.name, COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id, S.name

-- This will work as well: we applied an aggregate function to name. Since we know that all names in the group are the 
-- same, the maximum value will also be the same.
SELECT S.id, MAX(S.name), COUNT(F.spacecraft_id)
FROM Spacecraft S LEFT JOIN Flight F ON S.id=F.spacecraft_id
GROUP BY S.id
```
