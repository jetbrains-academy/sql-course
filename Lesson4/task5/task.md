Let's recall the query where we joined planets and flights to find for each planet all the flights to that planet.

```sql
SELECT *
FROM Planet JOIN Flight ON id = planet_id
```

This query returns a planet in the results only if there was at least one flight to that planet. But what if we
need *all* planets to be in the results, no matter if there were any flights or not, and just fill the flight
column values with `NULL`s for those planets which have no matching flights? If we had no special operator, this
task would be quite non-trivial. However, we're lucky because in the SQL join family, there are operators
designed exactly for that. Those operators are called outer joins.

### LEFT JOIN and RIGHT JOIN

The result of an outer join is a superset of an inner join, that is, it is always an inner join plus something
that we will call the "outer part". The set of rows in the outer part depends on the outer join type.

`LEFT OUTER JOIN` adds those rows from its left operand which did not join with any row from the right operand.
It adds every unmatched row just once and fills the values of the columns from the right operand with NULLs.
Consider the query:

```sql
SELECT *
FROM Planet LEFT OUTER JOIN Flight ON id = planet_id
```

Here, we output all rows from `Planet` with the joining flights, if there are any, and with `NULL`s if there are
no joining flights. The result looks as follows (only the join-relevant columns are shown; SQLite prints
booleans as `1`/`0` and empty cells for `NULL`):

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

The planets with flights appear with their flight data; the planets with no flights (Glacia, Dunar, Solmar,
Mirren, Cobar, Frost, Zephyra) each appear once, with `NULL`s in the flight columns.

Notice that left outer join is not commutative and its result is likely to be different if we swap the operands:

```sql
-- The result of this outer join will be the same as with inner join because there are no rows in Flight that
-- have no matching row in Planet.
SELECT *
FROM Flight LEFT OUTER JOIN Planet ON id = planet_id
```

Right outer join does the same thing, except that its outer part is built from the right operand rows with no
matches on the left side. Basically, `T LEFT JOIN R` is the same as `R RIGHT JOIN T` with the same join
condition. Right join may turn useful in some cases of join chains, as we will see in the next step.

```sql
SELECT *
FROM Flight RIGHT OUTER JOIN Planet ON id = planet_id
```

In the keywords `LEFT OUTER JOIN` and `RIGHT OUTER JOIN`, the word `OUTER` is optional, and it is okay to skip it.

### Full outer join

Full outer join adds both the "left" and the "right" outer parts to the inner join. In the results, we will have
one row for each planet with no flights and one row for each flight with no matching planet, if such flights exist.

```sql
SELECT *
FROM Planet FULL OUTER JOIN Flight ON id = planet_id
```

In our data every flight has a matching planet, so this full outer join returns the same rows as the left outer
join shown above.
