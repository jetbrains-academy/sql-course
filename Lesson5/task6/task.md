Aside from filters in the `WHERE` clause, which, as we know, are applied before grouping takes place, SQL allows for other filters,
which are applied during or after the work of `GROUP BY` and aggregate functions.

### Selective aggregates

When the grouping is done, we can additionally filter the rows, which are fed to each of the aggregate functions used in the query:

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

In the example above, we apply a filter in the `WHERE` clause to consider only the flights to the planets with a mild climate,
then group by planet id, and calculate three aggregates within each group. 
All three aggregate functions are `COUNT`; however, their values are different due to the additional filters defined 
by the `FILTER` keyword and the filtering expression.

The filtering expression may use what is allowed in the `WHERE` clause, with some restrictions.

Unfortunately, although `FILTER` has been in the SQL standard since 2003, it is an optional features and is barely supported by  
database engines. 
Basically, among the most popular databases, only PostgreSQL and SQLite support it out of the box. 
However, `FILTER` is easy to emulate using case expressions, which are supported by nearly every database engine. 

Case expressions are similar to ternary expressions in C++ or Java and when-expressions in Kotlin. They look like this:

```sql
CASE 
WHEN <condition1> THEN <result1>
WHEN <condition2> THEN <result2>
...
ELSE <default_result>
END
```

In case expressions, conditions are evaluated in the lexical order until one of them returns `true`. 
If none of the conditions returns `true`, `default_result` is returned, if specified; otherwise, the expression returns 
`NULL`.

We can use a case expression with one `WHEN` branch as an aggregate function expression 
and return the value to be aggregated if the condition evaluates to `true`, or `NULL` otherwise. 
As we remember, aggregate functions skip `NULL` values, so returning `NULL` from a case expression 
effectively filters out the input row. Our query can be rewritten using case expressions as follows:

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

### Filtering the whole group

Sometimes we want to drop the whole group if it doesn't match some criteria. 
For instance, what if we're looking for planets with a mild climate and the total count of flights not less than 3? 
We can do it with the `HAVING` clause:

```sql
SELECT P.id, P.name, COUNT(*)
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id, P.name
HAVING COUNT(*) >= 3
```

Lexically, it comes in a query after the `GROUP BY` clause and is evaluated after `GROUP BY`. 
It takes a boolean expression, which is applied to the entire group.
If the expression returns `true` for a group, that group goes further to `SELECT`; otherwise, the group is filtered out.

Since the expression applies to the entire group, it has the same restrictions as those in the `SELECT` clause: we can use 
only the values of the grouping columns and aggregate functions.



