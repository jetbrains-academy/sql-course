## Other ways to make inner joins

Aside from the explicit `JOIN` operators, there are other ways to join tables in SQL. 

### Cross-join and filters

Let's recall what we do when we join two tables: we consider all the pairs of rows and leave only those where some condition,
e.g. equality of some attributes, is met. The set of all pairs is a cartesian product of the sets or rows, and in SQL 
there is an operator `CROSS JOIN` which builds a cartesian product. For instance, this query:

```sql
SELECT * FROM Planet CROSS JOIN Flight
```

will build a cartesian product of rows fom `Planet` with rows from `Flight`.
Cartesian product per se is not very useful, especially when the number of rows in the tables is big, because the count of rows in 
its result is the multiplication of the count of rows in the operands. However, if we add a filter which leaves only those 
rows where our join condition is met, we get a join in the result. The following query is absolutely equivalent to 
inner join of `Planet` and `Flight` using `planet_id`: 

```sql
SELECT * FROM Planet CROSS JOIN Flight
WHERE Planet.planet_id=Flight.planet_id
```

`CROSS JOIN` can be replaced with a simple comma. Yes, comma in SQL is an operator (except when it is not):

```sql
SELECT * FROM Planet, Flight
WHERE Planet.planet_id=Flight.planet_id
```

Such way of writing joins used to be popular before the explicit `JOIN` operator came into the standard and was 
implemented in the major SQL engines.

### IN operator makes a join too

What if we want to find the dates of flights to uninhabited planets? We can write this query using joins:

```sql
SELECT flight_date FROM Flight JOIN Planet WHERE Planet.is_inhabited = FALSE
```

but if we recall subqueries in `WHERE` clause and `IN` operator, we may rewrite this query as follows:

```sql
SELECT flight_date FROM Flight
WHERE planet_id IN (SELECT id FROM Planet WHERE is_inhabited = FALSE)
```


