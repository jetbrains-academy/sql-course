## INNER JOIN operator

The standard way of finding pairs of matching rows from two tables in SQL is the operator called `INNER JOIN`, which
shall be used in `FROM` clause. 
It has a few variations, which all search for pairs matching some condition and differ only in a way how the match
condition is formulated.

### JOIN ... USING

If both tables have an attribute with the same name, and we want to match rows with the same value in this column,
we can use `JOIN..USING` variation which looks as follows:

```sql
SELECT * 
FROM Planet INNER JOIN Flight USING(planet_id)
```
[TODO: can we enumerate columns in using?]

The keyword `INNER JOIN` comes between the names of the tables being joined, while the join condition follows the 
keyword and both operands. Inner join is a commutative operation, so if we swap the table names, the result will 
be identical: 

```sql
SELECT * FROM Flight INNER JOIN Planet USING(planet_id)
```

Pay attention that commutativity is the inner join property, but some other operators from the family of joins are 
not commutative.

### JOIN ... ON

What if the matching attributes have different names, e.g. `Planet.id` and `Flight.planet_id`? 
In this case we can use a variation which uses an explicit join condition. We can write it using the keyword `ON` instead
of `USING`:

```sql
SELECT * 
FROM Planet INNER JOIN Flight ON Planet.id = Flight.planet_id
```

This option allows for using conditions other than equalities. What if we want for each flight find all flights made at 
the earlier dates, if there are any? We can easily do it like this:

```sql
SELECT * 
FROM Flight AS F1 INNER JOIN Flight AS F2 ON F1.flight_date > F2.flight_date
```

Logically this will work the same way: for each row `f1` from `Flight` table we will find all rows from the same 
`Flight` table, such that their flight date is less than the flight date of `f1`. Notice that we used table aliases 
`F1` and `F2` to distinguish between two logical "copies" of `Flight` table.

We can use any expression in a join condition, as soon as it returns a boolean value. For instance, if we want to find 
for each planet find all planets with the same climate and smaller radius, we can do it as follows:

```sql
SELECT * 
FROM Planet P1 INNER JOIN Planet P2 ON P1.climate = P2.climate AND P1.radius > P2.radius
```

### NATURAL JOIN

One more variation, which joins by equality of values in the columns with the same names, is so-called _natural join_:

```sql
SELECT * FROM Flight NATURAL JOIN Planet
```

This is equivalent to `USING` keyword which enumerates all columns with the same names from the joined tables. This 
may render useful if we don't know the set of join columns in advance or just too lazy to enumerate them all :) 

[TODO: is natural join supported everywhere?]
