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
are different, unless the engine can prove the opposite. Some engines are more optimistic though and allow for using
non-grouping columns in `SELECT` from grouped table, hoping that programmers won't shoot their own foot, and select arbitrary 
values for non-grouping columns.


