## Aggregate functions, groups and filters

Aside from filters in `WHERE` clause, which as we know are applied before grouping takes place, SQL allows for other filters
which are applied during or after the work of `GROUP BY` and aggregate functions.

### Selective aggregates

When the grouping is done, we can additionally filter the rows which are fed to each of the aggregate functions in a query:

```sql
SELECT P.id, 
       COUNT(*)                        AS total_flights, 
       COUNT(*) FILTER (S.capacity>5)  AS big_capacity_flights,
       COUNT(*) FILTER (S.capacity<=5) AS small_capacity_flights,
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id
```

In the example above, we apply filter in `WHERE` clause which leaves only flights to the planets with mild climate,
then group by planet id, and within each group calculate three aggregates. 
All three aggregate functions are `COUNT`, however their values are different because of the additional filters defined 
by `FILTER` keyword and filtering expression.

The filtering expression may use what is allowed in `WHERE` clause, with some restrictions.

Unfortunately, although `FILTER` is in SQL standard since 2003, it is an optional features and is barely supported by the 
database engines. 
Basically, out of the most popular databases, only PostgreSQL and SQLite support it out of the box. 
However, `FILTER` is easy to emulate using case-expressions which are supported by nearly every database engine. 

Case expressions are similar to ternary expressions in C++ or Java, and when-expressions in Kotlin. 
We can write a case-expression inside the aggregate function, and return the value to be aggregated if the condition 
returns `true`, and `NULL` otherwise. 
As we remember, aggregate functions skip `NULL` values, so returning `NULL` from case-expression 
effectively filters out the input row. Our query can be rewritten using case-expressions as follows:

```sql
SELECT P.id, 
       COUNT(*)                                            AS total_flights, 
       COUNT(CASE WHEN S.capacity>5 THEN 1 ELSE NULL END)  AS big_capacity_flights,
       COUNT(CASE WHEN S.capacity<=5 THEN 1 ELSE NULL END) AS small_capacity_flights,
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id
```

### Filtering the whole group

Sometimes we want to drop the whole group if it doesn't match some criteria. 
For instance, what if we're looking for planets with mild climate and the total count of flights not less than 20? 
We can do it with `HAVING` clause:

```sql
SELECT P.id, P.name, COUNT(*)
FROM Flight F JOIN Spacecraft S ON S.id=F.spacecraft_id
              JOIN Planet P     ON P.id=F.planet_id
WHERE P.climate='mild'
GROUP BY P.id, P.name
HAVING COUNT(*) >= 20
```

Lexically it comes in a query after `GROUP BY` clause and is evaluated after `GROUP BY`. 
It takes a boolean expression which is applied to the entire group.
If the expression returns `true` for a group, that group goes further to `SELECT`, otherwise the group is filtered out.

Since the expression applies to the entire group, it has the same restrictions as those in `SELECT` clause: we can use 
only the values of the grouping columns and aggregate functions.



