## Outer joins

Let's recall the query where we joined planets and flights to find for each planet all flights to that planet. 

```sql
SELECT *
FROM Planet JOIN Flight ON id=planet_id
```

This query will return a planet in the results if there was at least one flight to that planet. But what if we need
*all* planets to be in the results, no matter if there were any flights or not, and just fill the flight column values 
with `NULL`s for those planets which have no matching flights? If we had no special operator, this task would have been 
quite non-trivial. However, we're lucky, because in SQL join family there are operators designed exactly for that.
Those operators are called outer joins.

### LEFT JOIN and RIGHT JOIN

The result of an outer join is a superset of an inner join, that is, it is always an inner join plus something, which we will
call "outer part". The set of rows in the outer part depends on the outer join type. 

`LEFT OUTER JOIN` adds those rows from its left operand which did not join with any row from the right operand. It adds 
every unmatched row just once and fills the values of columns from the right operand with NULLs. If we write  

```sql
SELECT *
FROM Planet LEFT OUTER JOIN Flight ON id=planet_id
```

then we will output all rows from `Planet` with joining flights if there are any, and with `NULL`s if there are no joining 
flights. The result will look as follows:

| P.name | P.planet_id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|--------|-------------|----------------|-------|-------------|---------------|
| Disa   | 1           | true           | MF201 | 1           | 2122-04-12    |
| Reva   | 3           | true           | MF147 | 3           | 2122-05-01    |
| Reva   | 3           | true           | MF149 | 3           | 2122-05-08    |
| Disa   | 1           | true           | MF201 | 1           | 2122-05-12    |
| Lava   | 2           | false          | NULL  | NULL        | NULL          |
| Tibela | 4           | NULL           | NULL  | NULL        | NULL          |

Notice that left outer join is not commutative, and its result is likely to be different if we swap the operands:

```sql
-- The result of this outer join will be the same as inner join, because there are no rows in Flight which have no 
-- matching row in Planet.
SELECT *
FROM Flight LEFT OUTER JOIN Planet ON id=planet_id
```


Right outer join does the same thing, except that its outer part is built from the right operand rows with no matches 
on the left side. Basically, `T LEFT JOIN R` is the same as `R RIGHT JOIN T` with the same join condition. Right join 
may turn useful in some cases of join chains, as we will see further.

```sql
SELECT *
FROM Flight RIGHT OUTER JOIN Planet ON id=planet_id
```

[TODO: provide examples of left join chains]

In the keywords `LEFT OUTER JOIN` and `RIGHT OUTER JOIN` the word `OUTER` is optional, and it is okay to skip it.

### Full outer join

Full outer join adds both "left" and "right" outer parts to the inner join. We will have in the results one row for each 
planet with no flights and one row for each flight with no planets, if we assume that such flights may exist. 

```sql
SELECT *
FROM Planet FULL OUTER JOIN Flight ON id=planet_id
```


[TODO: illustrate inner and outer joins with a picture]