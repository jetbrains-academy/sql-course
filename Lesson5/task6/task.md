In addition to filters in the `WHERE` clause – which, as we know, are applied before grouping takes place – SQL supports filters
that operate after `GROUP BY` and aggregate functions have processed the data.

### Selective aggregates

Once grouping is complete, we can further filter which are passed to individual aggregate functions in the query:

```sql
SELECT P.id,                                                      --
       COUNT(*)                        AS total_flights,          --
       COUNT(*) FILTER (WHERE S.capacity>5)  AS big_capacity_flights,   --
       COUNT(*) FILTER (WHERE S.capacity<=5) AS small_capacity_flights  --
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id           -- 1. FROM is executed first
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'                                            -- 2. Filtering 
GROUP BY P.id                                                     --
```

In the example above, the `WHERE` clause filters rows to include only flights to planets with a mild climate,
The query then groups the remaining rows by planet ID and computes three aggregates for each group. 
Although all three use `COUNT`, their outputs differ due to the conditions specified 
in their respective `FILTER` clauses.

The filtering expression supports most conditions allowed in a `WHERE` clause, with a few restrictions.

Although `FILTER` has been part of the SQL standard since 2003, it is an optional feature with limited native support across  
database engines. 
Among the most popular databases, only PostgreSQL and SQLite support it out of the box. 
However, `FILTER` can easily be emulated using `CASE` expressions, which are supported by virtually every database engine. 

`CASE` expressions are similar to ternary operators in C++ or Java, or `when` expressions in Kotlin. They look like this:

```sql
CASE 
WHEN <condition1> THEN <result1>
WHEN <condition2> THEN <result2>
...
ELSE <default_result>
END
```

In `CASE` expressions, conditions are evaluated in lexical order until one of them evaluates to `true`. 
If none of the conditions match, the specified `default_result` is returned; otherwise, the expression evaluates to 
`NULL`.

We can place a `CASE` expression with a single `WHEN` branch inside an aggregate function, 
returning the value to be aggregated when the condition evaluates to `true` and `NULL` otherwise. 
Recall that aggregate functions ignore `NULL` values, so returning `NULL` from a `CASE` expression 
effectively filters out non-matching rows. Our query can be rewritten using `CASE` expressions as follows:

```sql
SELECT P.id, 
       COUNT(*)                                            AS total_flights, 
       COUNT(CASE WHEN S.capacity>5 THEN 1 ELSE NULL END)  AS big_capacity_flights,
       COUNT(CASE WHEN S.capacity<=5 THEN 1 ELSE NULL END) AS small_capacity_flights
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id
```

### Filtering entire groups

Sometimes we want to exclude an entire group if it doesn't meet certain criteria. 
For instance, what if we're looking for planets with a mild climate that have at least 3 total flights? 
We can achieve this using the `HAVING` clause:

```sql
SELECT P.id, P.name, COUNT(*)
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id, P.name
HAVING COUNT(*) >= 3
```

In a query, the `HAVING` clause appears after `GROUP BY` and is evaluated after `GROUP BY`. 
It accepts a boolean expression that is applied to the entire group.
If the expression evaluates to `true`, the group is passed to the `SELECT` clause; otherwise, it is filtered out.

Because the condition applies to the entire group, it follows the same rules as `SELECT`: it can reference 
only grouping columns and aggregate functions.



