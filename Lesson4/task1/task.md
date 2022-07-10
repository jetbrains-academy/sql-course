## INNER JOIN operator

The standard way of finding pairs of matching rows from two tables in SQL is the operator called `INNER JOIN`, which
shall be used in `FROM` clause. 
It has a few variations, which all search for pairs matching some condition and differ only in a way how the match
condition is formulated.

### JOIN ... ON

The most common and widely used syntax of join operator includes keywords `INNER JOIN` and `ON`.
The keywords `INNER JOIN` come between the names of the tables being joined, while the join condition, which
defines whether two rows join or not, follows the pair of tables after `ON` keyword: 

```sql
SELECT * 
FROM Planet INNER JOIN Flight ON Planet.id = Flight.planet_id
```

The keyword `INNER` is optional and can be skipped. Just `JOIN` means `INNER JOIN`:

```sql
SELECT * 
FROM Planet JOIN Flight ON Planet.id = Flight.planet_id
```

Inner join is a commutative operation, so if we swap the table names, the result will
be identical:

```sql
SELECT * FROM Flight JOIN Planet ON Planet.id = Flight.planet_id
```

Pay attention that commutativity is the property of inner join, but some other operators from the family of joins are
not commutative.

We can use conditions other than equalities. What if we want to find for each flight all flights made at
the earlier dates, if there are any? We can easily do it like this:

```sql
SELECT * 
FROM Flight AS F1 JOIN Flight AS F2 ON F1.flight_date > F2.flight_date
```

Logically this will work the same way as in case of equality: for each row `f1` from `Flight` table we will find all rows from the same
`Flight` table, such that their flight date is less than the flight date of `f1`. Notice that we used table aliases
`F1` and `F2` to distinguish between two logical "copies" of `Flight` table.

We can use any expression in a join condition, as soon as it returns a boolean value. For instance, if we want  
for each planet find all planets with the same climate and smaller radius, we can do it as follows:

```sql
SELECT * 
FROM Planet P1 JOIN Planet P2 ON P1.climate = P2.climate AND P1.radius > P2.radius
```

Two rows will join if the join expression for them returns `true`. If it returns `false` or `unknown`, the rows 
will not join.

### JOIN ... USING

If a join condition is an equality of columns and joining columns have the same name,
we can use `JOIN..USING` variation. Let's assume that we renamed `Planet.id` to `Planet.planet_id`. With the keyword `USING`
instead of `ON` we can just write the name of the join column:

```sql
-- This join query:
SELECT *
FROM Planet JOIN Flight ON Planet.planet_id = Flight.planet_id

-- Can be rewritten as follows:
SELECT * 
FROM Planet JOIN Flight USING(planet_id)
```

### NATURAL JOIN

One more variation, which joins by equality of values in the columns with the same names, is so-called _natural join_:

```sql
SELECT * FROM Flight NATURAL JOIN Planet
```

This is equivalent to `USING` keyword which enumerates all columns with the same names from the joined tables. This 
may render useful in some cases when we don't know the set of join columns in advance. However, one shall be careful 
when using `NATURAL JOIN` because it may suddenly start producing wrong results if joining tables get columns with 
the same name and type, but different semantics. Imagine, for instance, that we added a column named `people_count` to 
both `Flight` and `Planet` tables, assuming that it would mean "the number of a flight crew members" in the former and 
"the number of people living on a planet" in the latter. 
Their semantics is different, and the sets of possible values are likely to be different as well,
but natural join in this query will nevertheless use these columns in addition to `planet_id`:

```sql
-- With additional people_count column in both tables, this query
SELECT * FROM Flight NATURAL JOIN Planet

-- will be equivalent to this
SELECT * FROM Flight INNER JOIN Planet 
SELECT * FROM Flight F JOIN Planet P ON F.planet_id=P.planet_id AND F.people_count=P.people_count
```

and most likely we will always get an empty result.

----

Some inner join variations are not supported by some database engines, in particular, `NATURAL JOIN` and `JOIN...USING`
is not supported by Microsoft SQL Server. However, `JOIN...ON` syntax is likely to be supported by any engine.