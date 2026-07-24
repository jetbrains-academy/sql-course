An aggregate function takes a list of values and returns a single aggregate value for that list. For instance, 
an aggregate value may be the number of elements in the list, or the maximum value, if it makes sense, or the sum
of values for numeric input. The list of supported functions differs across the database engines, but there are five 
standard functions that are supported by all relational databases:

| Function | Description                                                     |
|----------|-----------------------------------------------------------------|
| COUNT    | Counts the number of elements in the input list                 |
| MAX      | Finds the maximum value in the input list                       |
| MIN      | Finds the minimum value in the input list                       |
| SUM      | Finds the sum of values in the input list                       |
| AVG      | Finds the average of values in the input list                   |

Aggregate functions can be used in `SELECT` and some other clauses, and it is important to understand
the logical model of calculating them, that is, how they work from the user's perspective.

First, let's look at how we build the list of input values. While it is tempting to write something like `SELECT SUM(1, 2, 3)`,
hoping to get the sum `1+2+3`, in reality things are more complicated. 

An aggregate function in SQL is parameterized with an expression that builds the input list of values. 
Aggregate function expressions are just like those used in the `SELECT` clause. 
They may include column names, and very often there is nothing else but the column name. 
When a database engine executes a query, it first builds all joins in the `FROM` clause, then filters the result using the `WHERE` clause, 
and after that feeds the result to the aggregate functions. 
Aggregate function expressions are evaluated for each row, and the aggregation input list is built from the values produced by 
the expressions.

For instance, if we want to find the maximum radius of all planets or the planets with a mild climate, we can do it like this:

```sql
-- This will find the maximum across all planets.
SELECT MAX(radius) FROM Planet;

-- This will find the average radius in miles across the planets with a mild climate
SELECT AVG(radius/1.609) FROM Planet WHERE climate = 'mild';
```

An interesting case is the `COUNT` function. 
It just counts the values in the input, and very often it does not matter what is counted. 
The following queries will all return the same result in our database:

```sql
SELECT COUNT(id) FROM Planet;
SELECT COUNT(name) FROM Planet;
SELECT COUNT(42) FROM Planet;
```

Even if the expression is just a constant, with the same value for any row in the `FROM-WHERE` output, `COUNT` will 
count it as many times as it appears. 
One may think that it doesn't matter what expression is used in `COUNT`, however, there are some subtle details which 
we should be aware of. Keep on reading!  

### Handling of NULL values

What if there are `NULL` values in the aggregate function input? It may happen if a table column is nullable 
and there are `NULL` values indeed, or if the expression evaluates to `NULL` because of some other reasons. 
What if there are _only_ `NULL` values in the input? What if the input is empty?

All aggregate functions, including `COUNT`, ignore `NULL` values in the input list. 
The `Flight.cargo` column is `NULL` for flights that carried no cargo, so the following queries return different results:

```sql
-- Returns 10, which is less than the number of rows, because cargo is NULL for some flights
SELECT COUNT(cargo) FROM Flight;

-- Returns 14 (the number of rows), because the input list includes the value 42 for every row
SELECT COUNT(42) FROM Flight;
```

There is a special syntax `COUNT(*)`, which means "just count the rows". 
It is helpful if counting the rows is really what you meant to do. 
However, it may not be so as we will see below. 

All aggregate functions except for `COUNT` return `NULL` if all input values are `NULL` or if the input is empty. 
`COUNT` is the only standard aggregate function that never returns `NULL`. 
If the input is empty or if all input values are `NULL`, `COUNT` will return `0`.

### Handling of distinct values

By default, the aggregate function input is a list, which allows for duplicates. 
However, sometimes we may want to remove duplicates and calculate distinct values only. 

Imagine that we want to count all planets visited by a spacecraft "Pegasus" and calculate their average radius. 
Here a "visited planet" means "there was at least one flight to that planet". 
Those queries below looks nice, right?

```sql
-- "Count" the planets visited by Pegasus
SELECT COUNT(*)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'

-- Calculate the average radius of the planets visited by Pegasus
SELECT AVG(P.radius)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'
```

Unfortunately, their results are wrong unless there is a happy coincidence. 
The output of joining planets, flights, and spacecraft is a table where the 
same planet may appear several times – as many times as there were flights to that planet. 
Aggregate functions will count the flights made by Pegasus, not the planets, and will find the average value across 
the flights, which is not the same as the average radius of the visited planets.

The good news is that we can easily fix this issue. Adding a keyword `DISTINCT` before the aggregate function expression 
will make it count only distinct values. 


Ooops, it is not _that_ easy. `*` is not an expression. 
If we want distinct values, we need to write the expression that produces the values. 
Now we should be careful and choose the expression wisely. 
For instance, `COUNT(DISTINCT 42)` will always return `1`, no matter how many rows and different planets are in the result. 
Let's count the planet identifiers:

```sql
-- Really count the planets visited by Pegasus
SELECT COUNT(DISTINCT P.id)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'
```


Okay, it certainly looks better. Why don't we use the same approach to calculating the average radius? 

```sql
SELECT AVG(DISTINCT P.radius)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'
```

Is this query correct? Well, the answer is "it depends". We aggregate the distinct values of the "radius" attribute. 
This will work if different planets always have different radiuses, but can we be sure that it is really the case? 
What if just by coincidence two different planets happen to have the same radius? 
In this case, we will feed it to `AVG` only once, and the result will obviously be wrong.


Unless we have a guarantee that planet radius values are always different for different planets, we need some other way 
to solve the problem, and we will consider that in the next lesson.

### Restrictions on SELECT clause

If you're using at least one aggregate function in the `SELECT` list, you can't use any other expression in that list 
unless it is an aggregate function as well:

```sql
-- This works
SELECT MAX(radius), MIN(radius) FROM Planet

-- This doesn't work
SELECT id, name, MAX(radius) FROM Planet
```

The reason sits in the SQL standard, which says that in the presence of aggregate functions in the `SELECT` clause, the result
shall contain 1 row unless there are groups, which we will discuss later. 
If the result is just a single row with the aggregate function values, the database engine can't add there any "bare" 
non-aggregated column values because there is no rule to chose one out of N values in all table rows.

However, there are valid use cases where an aggregate function is used along with bare column values. 
For instance, if we want to output all planet rows with all their columns _and_ an additional column with the maximum 
radius across all planets. 
There are a few ways to achieve such results, which we will discuss later.  

### Scalar subqueries

A query that returns a single aggregated value is called a scalar query. 
A nice feature of a scalar query is that its return value can be used almost anywhere where a simple scalar expression is valid. 

For instance, we can easily find the value of the maximum radius:

```sql
SELECT MAX(radius) FROM Planet
```

But how can we find the planet with this radius? It is easy using a scalar subquery in the `WHERE` clause. Just compare the maximum 
with the current row `radius` attribute:

```sql
SELECT * FROM Planet WHERE radius = (SELECT MAX(radius) FROM Planet)
```

It is also possible to use scalar subqueries in the `SELECT` clause and this way to solve the problem mentioned above – 
output all rows and columns from `Planet` with an additional column with the maximum radius:

```sql
SELECT id, name, radius, climate, (SELECT MAX(radius) FROM Planet) AS max_radius
FROM Planet
```

However, this approach should be used with care because database engines may fail to execute such queries efficiently.



