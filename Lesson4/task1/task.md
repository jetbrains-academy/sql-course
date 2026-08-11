The standard way to match rows from two tables in SQL is using the `INNER JOIN` operator
inside the `FROM` clause. 
It comes in a few variations, which all search for matching row pairs and differ only in how the match
condition is formulated.

### JOIN ... ON

The most common and flexible syntax uses the `INNER JOIN` and `ON` keywords.
The `INNER JOIN` phrase goes between the names of the tables being joined, while the match condition
following the `ON` keyword defines which row pairs should be combined: 

```sql
SELECT * 
FROM Planet INNER JOIN Flight ON Planet.id = Flight.planet_id
```

The `INNER` keyword is optional. Writing just `JOIN` defaults to `INNER JOIN`:

```sql
SELECT * 
FROM Planet JOIN Flight ON Planet.id = Flight.planet_id
```

An inner join is a commutative operation, so if we swap the table names, the result will
be identical:

```sql
SELECT * FROM Flight JOIN Planet ON Planet.id = Flight.planet_id
```

Note: Commutativity is a property of inner joins. Other join types are
non-commutative.

Join conditions are not limited to equality. Suppose you want to find all previous flights for each flight in the table.
You can easily do it like this:

```sql
SELECT * 
FROM Flight AS F1 JOIN Flight AS F2 ON F1.flight_date > F2.flight_date
```

Logically, this works just like an equality join: for each row `F1` from the `Flight` table we find all rows from the same
table where flight date is earlier. Notice that we used table aliases
`F1` and `F2` to distinguish between the two logical "copies" of the `Flight` table.

In a join condition, you can use any valid boolean expression. For instance, if we want  
to find all pairs of planets with the same climate where the first planet is larger than the second, we can do it as follows:

```sql
SELECT * 
FROM Planet P1 JOIN Planet P2 ON P1.climate = P2.climate AND P1.radius > P2.radius
```

Two rows join only if the join expression for them returns `true`. If it returns `false` or `unknown`, the rows 
will not join.

### JOIN ... USING

When joining tables on equality between columns that share the exact same name,
you can use the `JOIN...USING` variation. Let's assume that we renamed `Planet.id` to `Planet.planet_id`. With the `USING` keyword
instead of `ON`, you can simply specify the name of the join column:

```sql
-- This join query:
SELECT *
FROM Planet JOIN Flight ON Planet.planet_id = Flight.planet_id

-- Can be rewritten as follows:
SELECT * 
FROM Planet JOIN Flight USING(planet_id)
```

### NATURAL JOIN

Another variation that joins tables based on the equality of values in columns with the same names, is the so-called _natural join_:

```sql
SELECT * FROM Flight NATURAL JOIN Planet
```

This is equivalent to the `USING` keyword, which returns all columns with the same name from the joined tables. This 
can be convenient when you don't know the exact join columns in advance. However, you need to be cautious 
when using `NATURAL JOIN`: it can yield incorrect results if the joining tables have columns with 
the same name and data type but with different semantics. Imagine, for instance, adding a column named `people_count` to 
both the `Flight` and `Planet` tables, representing "the number of crew members" in the former and 
"population" in the latter. 
Their semantics are different, and the sets of possible values are likely to be different as well,
but a natural join will nevertheless match on `people_count` alongside `planet_id`:

```sql
-- With additional people_count column in both tables, this query
SELECT * FROM Flight NATURAL JOIN Planet

-- will be equivalent to this
SELECT * FROM Flight F JOIN Planet P ON F.planet_id=P.planet_id AND F.people_count=P.people_count
```

Most likely, we will always get an empty result because the number of crew members will almost never equal
the total population of the destination planet.

----

Some inner join variations are not supported by certain database engines. For example, Microsoft SQL Server does not support `NATURAL JOIN` and `JOIN...USING`.
However, the `JOIN...ON` syntax is likely to be supported by any engine.
