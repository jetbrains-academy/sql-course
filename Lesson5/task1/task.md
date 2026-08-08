An aggregate function takes a list of values and returns a single aggregate value. For instance, 
an aggregate value may be the total count of elements, the maximum value (where applicable), or the sum
of numeric inputs. While supported functions vary across database engines, all relational databases support these five 
standard functions:

| Function | Description                                                     |
|----------|-----------------------------------------------------------------|
| COUNT    | Counts the number of elements in the input list                 |
| MAX      | Finds the maximum value in the input list                       |
| MIN      | Finds the minimum value in the input list                       |
| SUM      | Calculates the sum of values in the input list                  |
| AVG      | Calculates the average of values in the input list              |

Aggregate functions can be used in `SELECT` and several other clauses. It is important to understand
their logical execution model, that is, how they function from the user's perspective.

First, let's look at how the list of input values is built. While it might be tempting to write something like `SELECT SUM(1, 2, 3)`,
hoping to compute `1+2+3`, SQL aggregate functions actually operate quite differently. 

An aggregate function in SQL is parameterized by an expression that builds the input list of values. 
Aggregate function expressions work just like any standard expression in a `SELECT` clause: 
they can include column names, and very often consist of a single column reference. 
When the database engine executes a query, it first builds all joins specified in the `FROM` clause, then filters the result using the `WHERE` clause, 
and finally passes the result to the aggregate function. 
An aggregate function's expression is evaluated for each row, and the aggregation input list is built from the values produced by 
that expression.

For instance, if we want to find the maximum radius across all planets (or specifically among planets with a mild climate), we can write:

```sql
-- This will find the maximum across all planets.
SELECT MAX(radius) FROM Planet;

-- This will find the average radius in miles across the planets with a mild climate.
SELECT AVG(radius/1.609) FROM Planet WHERE climate = 'mild';
```

An interesting case is that of the `COUNT` function. 
It simply counts the values in its input list, and in many cases, the exact expression passed to it doesn't change the result. 
For instance, the following queries all return the exact same result on our database:

```sql
SELECT COUNT(id) FROM Planet;
SELECT COUNT(name) FROM Planet;
SELECT COUNT(42) FROM Planet;
```

Even if the expression is a simple constant, yielding the same value for every row output by the `FROM` and `WHERE` clauses, `COUNT` still 
counts every instance. 
It might seem like the choice of expression in `COUNT` makes no difference. However, there are a few subtle nuances to 
keep in mind. Keep reading to see how they work!  

### Handling NULL values

What happens when there are `NULL` values in the aggregate function input? This can occur if a table column is nullable 
and contains `NULL`s, or if an expression evaluates to `NULL`. 
What if the input contains _only_ `NULL` values? What if the input itself is empty?

All aggregate functions, including `COUNT`, ignore `NULL` values in their input lists. 
In our dataset, the `Flight.cargo` column is `NULL` for flights that carried no cargo, which means the following queries return different results:

```sql
-- Returns 10, which is less than the total number of rows, because cargo is NULL for some flights
SELECT COUNT(cargo) FROM Flight;

-- Returns 14 (the total number of rows), because the input list includes the value 42 for every row
SELECT COUNT(42) FROM Flight;
```

SQL provides a special syntax, `COUNT(*)`, which specifically means "count all rows". 
It is ideal when your goal is simply to count total rows in the result set. 
However, as we will see below, this isn't always the case! 

All aggregate functions except `COUNT` return `NULL` if the input list is empty or contains only `NULL` values. 
`COUNT` is unique in that it never returns `NULL`. 
If the input is empty or contains only `NULL` values, `COUNT` returns `0`.

### Handling distinct values

By default, the input to an aggregate function is a list, which allows duplicates. 
However, you often need to eliminate duplicates and evaluate only distinct values. 

Suppose we want to count all planets visited by the spacecraft _Pegasus_ and calculate their average radius. 
Here, a "visited planet" means a planet that had at least one flight. 
The queries below look reasonable at first glance, right?

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

Unfortunately, their results will be incorrect unless by pure coincidence. 
The output of joining planets, flights, and spacecraft is a table where a 
single planet may appear multiple times – once for every flight to that planet. 
Aggregate functions will count total flights by _Pegasus_ rather than distinct planets, and calculate the radius across 
all flights rather than across the unique visited planets.

The good news is that we can easily fix this. Adding the `DISTINCT` keyword inside the aggregate function 
instructs it to evaluate only distinct values. 


Ooops, it is not quite  _that_ simple. `*` is not an expression. 
If we want distinct values, we must supply a specific expression that produces the values. 
You also need to choose that expression carefully. 
For instance, `COUNT(DISTINCT 42)` will always return `1`, regardless of how many rows or planets are in the result. 
Let's count the actual planet identifiers instead:

```sql
-- Really count the planets visited by Pegasus
SELECT COUNT(DISTINCT P.id)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'
```


Okay, that certainly looks better. Why don't we use the same approach to calculating the average radius? 

```sql
SELECT AVG(DISTINCT P.radius)
FROM Planet P JOIN Flight F ON P.id=F.planet_id JOIN Spacecraft S ON S.id=F.spacecraft_id
WHERE S.name='Pegasus'
```

Is this query correct? Well, the answer is "it depends". Here, we're aggregating the distinct values of the `radius` attribute. 
This works if every planet has a unique radius, but can we be guarantee that's always the case? 
What if, by pure coincidence, two different planets happen to have the exact same radius? 
In that scenario, `AVG` will only count that radius value once, producing an incorrect result.


Unless we have a guarantee that every planet has a unique radius, we need a different approach 
to solve this problem – which we will cover in the next lesson.

### Restrictions on the SELECT clause

If you use an aggregate function in the `SELECT` clause, every other item in that `SELECT` list 
must also be an aggregate function:

```sql
-- This works
SELECT MAX(radius), MIN(radius) FROM Planet

-- This doesn't work
SELECT id, name, MAX(radius) FROM Planet
```

This restriction comes directly from the SQL standard: when a query contains aggregate functions in the `SELECT` clause, the query
must return exactly 1 row (unless there are specific groups that we will discuss later). 
If the result is a single row containing aggregated values, the database engine cannot include any "bare" 
(unaggregated) column values because there is no logical rule to pick just one row's values out of many.

However, there are valid use cases for using an aggregate function along with bare column values. 
For instance, you might want to display every column for all planets alongside an additional column showing the overall maximum 
planet radius. 
There are several ways to achieve this, which we will explore in later lessons.  

### Scalar subqueries

A query that returns a single aggregate value is called a scalar query. 
A key advantage of scalar queries is that their return value can be used almost anywhere a simple scalar expression is valid. 

For instance, finding the the maximum planet radius is straightforward:

```sql
SELECT MAX(radius) FROM Planet
```

Now, how do we find the specific planet that has this maximum radius? You can easily do this by placing a scalar subquery in the `WHERE` clause to compare each row's `radius` against that maximum 
value:

```sql
SELECT * FROM Planet WHERE radius = (SELECT MAX(radius) FROM Planet)
```

You can also use scalar subqueries in the `SELECT` clause. This solves the problem mentioned earlier – 
displaying all rows and columns from `Planet` alongside an additional column showing the overall maximum radius:

```sql
SELECT id, name, radius, climate, (SELECT MAX(radius) FROM Planet) AS max_radius
FROM Planet
```

However, use this approach with caution, as some database engines might not execute such queries efficiently.



