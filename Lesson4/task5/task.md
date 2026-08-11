Let's recall the query where we joined planets and flights to find all flights heading to each planet.

```sql
SELECT *
FROM Planet JOIN Flight ON id = planet_id
```

This query returns a planet in the results only if there was at least one flight to that planet. But what if we
want *all* planets included in the results, regardless of whether they have scheduled flights, and poopulate the flight
column with `NULL` values for planets that have no matching flights? Without a dedicated operator, this
task would be quite complex. Fortunately, SQL includes a family of operators
designed specifically for this purpose: outer joins.

### LEFT JOIN and RIGHT JOIN

The result of an outer join is a superset of an inner join, that is, it always includes the inner join result plus an additional set of rows
we call the "outer part". The exact rows in the outer part depend on the outer join type.

A `LEFT OUTER JOIN` preserves all rows from its left operand that did not match any row in the right operand.
It includes each unmatched row exactly once, filling the right-operand columns with NULL values.
Consider the following query:

```sql
SELECT *
FROM Planet LEFT OUTER JOIN Flight ON id = planet_id
```

Here, we output all rows from `Planet` alongside their matching flights (if any), filling `NULL`s where
no matching flights exist. The result is shown below (only the join-relevant columns are included). Note: SQLite displays
booleans as `1`/`0` and renders `NULL` values as empty cells.

| P.name  | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|---------|------|----------------|-------|-------------|---------------|
| Terra   | 1    | 1              | AF201 | 1           | 2122-04-12    |
| Aquara  | 2    | 1              | AF210 | 2           | 2122-05-12    |
| Pyros   | 3    | 0              | AF088 | 3           | 2122-06-15    |
| Glacia  | 4    | 0              | NULL  | NULL        | NULL          |
| Verda   | 5    | 1              | AF147 | 5           | 2122-05-01    |
| Verda   | 5    | 1              | AF149 | 5           | 2122-05-08    |
| Dunar   | 6    | 0              | NULL  | NULL        | NULL          |
| Solmar  | 7    | 0              | NULL  | NULL        | NULL          |
| Mirren  | 8    | 1              | NULL  | NULL        | NULL          |
| Cobar   | 9    | 0              | NULL  | NULL        | NULL          |
| Frost   | 10   | 0              | NULL  | NULL        | NULL          |
| Zephyra | 12   | 0              | NULL  | NULL        | NULL          |
| Answer  | 42   | 1              | AF305 | 42          | 2122-06-01    |

Planets with flights appear with their flight details. Planets without flights (Glacia, Dunar, Solmar,
Mirren, Cobar, Frost, Zephyra) each appear once, with `NULL`s in the flight columns.

Notice that `LEFT OUTER JOIN` is not commutative. Swapping the operands will likely produce a different result:

```sql
-- The result of this outer join is identical to an inner join because all rows in Flight
-- have a matching row in Planet.
SELECT *
FROM Flight LEFT OUTER JOIN Planet ON id = planet_id
```

A `RIGHT OUTER JOIN` works the same way, except its outer part is built from the right-operand rows that have no
matches on the left. In short, `T LEFT JOIN R` is functionally identical to `R RIGHT JOIN T` using the same join
condition. As we will see in the next step, right joins may be particularly useful in some cases of join chains.

```sql
SELECT *
FROM Flight RIGHT OUTER JOIN Planet ON id = planet_id
```

In `LEFT OUTER JOIN` and `RIGHT OUTER JOIN`, the `OUTER` keyword is optional and you can skip it.

### FULL OUTER JOIN

A `FULL OUTER JOIN` adds both the "left" and "right" outer parts to the inner join result. Consequently, the output will include
a row for each planet without flights, as well as a row for each flight without a matching planet (if any such flights exist).

```sql
SELECT *
FROM Planet FULL OUTER JOIN Flight ON id = planet_id
```

In our dataset, every flight has a matching destination planet. Therefore, in this specific case, a `FULL OUTER JOIN` produces the exact same output as the
`LEFT OUTER JOIN` above.
