Aside from explicit `JOIN` clauses, SQL provides alternative ways to join tables. 

### CROSS JOIN and filters

Let's recall what we do when we join two tables. 
We evaluate all possible pairs of rows and keep only those that meet a specific condition, such as equality between attributes. 
The set of all pairs is the Cartesian product of the two sets of rows. In SQL, the `CROSS JOIN` operator 
produces this exact Cartesian product. 
For instance, the following query builds a Cartesian product of rows from `Planet` and `Flight`:

```sql
SELECT * FROM Planet CROSS JOIN Flight
```

A Cartesian product by itself is rarely useful, especially with large tables, because the count of rows in 
its result equals the product of the row counts of both tables. However, adding a filter to keep only the 
rows that meet our join condition produces a standard join. The following query is completely equivalent to the 
inner join between `Planet` and `Flight` on the `id` and `planet_id` columns: 

```sql
SELECT * FROM Planet CROSS JOIN Flight
WHERE Planet.id=Flight.planet_id
```

You can also replace `CROSS JOIN` with a simple comma. Yes, a comma in the `FROM` clause is an operator:

```sql
SELECT * FROM Planet, Flight
WHERE Planet.id=Flight.planet_id
```

This syntax was popular before explicit `JOIN` operators were added to the standard and  
implemented across major SQL engines.

### Using IN to join data

Suppose we want to find the dates of all flights destined for uninhabited planets. We can write such a query using joins and filters:

```sql
SELECT flight_date FROM Flight F JOIN Planet P ON F.planet_id=P.id WHERE P.is_inhabited = false
```

Alternatively, if we recall subqueries in the `WHERE` clause and the `IN` operator, we can rewrite this query as follows:

```sql
SELECT flight_date FROM Flight
WHERE planet_id IN (SELECT id FROM Planet WHERE is_inhabited = false)
```

Here, the subquery returns a list of identifiers for uninhabited planets. The outer query then checks whether each flight's 
`planet_id` value exists within that list, achieving the same result as an inner join with a filter.

We might as well write this query using a cross join and a filter:

```sql
SELECT * FROM Planet P, Flight F
WHERE P.id=F.planet_id
  AND P.is_inhabited=false
```

### Which approach is better?

Thus, we have several ways to write a query that produce identical results. You might wonder whether there are any
significant differences between them and which approach is best. 
The short answer is: use explicit joins and filters. However, there are a few important aspects to consider.

1. *Code readability and maintainability*. This is the most crucial factor because you write code primarily for human readers. 
Computers don't care how clearly you express your intent, but it is important that anyone reading your 
code in the future can easily understand what you meant. 

From this perspective, when joining tables, always use `JOIN` because it makes your intent clear. Unlike 
a cross join, where technical join conditions get mixed in with important filters within a long `WHERE` clause, an explicit `JOIN`  
separates these concerns. This makes it clear which is the filter and which is the join condition. Consider the following two queries, which both 
find planets visited by large spacecraft built in shipyards located on hot-climate planets
(note: the `Shipyard` table used here is hypothetical and solely for
illustration):

```sql
SELECT P.name
-- This is clearly a chain of joins with join conditions
FROM Planet P JOIN Flight F ON P.id=F.planet_id
JOIN Spacecraft S ON S.id=F.spacecraft_id
JOIN Shipyard Y ON S.shipyard_id=Y.id
JOIN Planet P2 ON P2.id=Y.planet_id
-- This is clearly a search filter
WHERE P2.climate = 'hot' AND S.capacity > 10
```

```sql
SELECT P.name
FROM Planet P, Flight F, Spacecraft S, Shipyard Y, Planet P2
-- Here, join conditions and filters are mixed, and it is difficult to distinguish between them
WHERE P.id=F.planet_id 
  AND S.shipyard_id=Y.id 
  AND P2.climate = 'hot' 
  AND P2.id=Y.planet_id 
  AND S.id=F.spacecraft_id
  AND S.capacity > 10
```

Furthermore, outer joins can't be easily expressed using cross joins and filters. While a Cartesian product can find matching
pairs, it cannot easily identify rows that have no match in the opposite table. 

Subqueries and the `IN` operator can sometimes make a query more understandable, but they often add nested structures that 
reduce overall readability. Consider the same query rewritten using nested subqueries:

```sql
SELECT name FROM Planet WHERE id IN (
    SELECT planet_id FROM Flight WHERE spacecraft_id IN (
        SELECT id FROM Spacecraft WHERE capacity>10 AND shipyard_id IN (
            SELECT id FROM Shipyard WHERE planet_id IN (
                SELECT id FROM Planet WHERE climate = 'hot'
                )
            )
        )
    )
```

This code is difficult to read because it contains four levels of nesting and scatters the search filter conditions across 
different blocks. Besides, the top-level select restricts us to attributes from the `Planet` table alone. 
We can't select details like a flight date or spacecraft capacity alongside the planet name.

2. *Performance*. In many cases, a modern database engine will execute all these queries identically. 
Whether you use an explicit join, `IN`, or `CROSS JOIN`, the engine will use a join algorithm under the hood, 
resulting in equivalent performance. 
However, execution speed ultimately depends on the quality of the database query optimization system and factors 
like table sizes and statistics. 
In general, it is impossible to claim a priori that any one of these approaches is inherently more efficient than the others.
