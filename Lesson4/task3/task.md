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
its result is the multiplication of the counts of rows in the operands. However, if we add a filter which leaves only those 
rows where our join condition is met, we get a join in the result. The following query is absolutely equivalent to 
inner join of `Planet` and `Flight` on `id` and `planet_id` columns: 

```sql
SELECT * FROM Planet CROSS JOIN Flight
WHERE Planet.id=Flight.planet_id
```

`CROSS JOIN` can be replaced with a simple comma. Yes, comma in SQL is an operator (except when it is not):

```sql
SELECT * FROM Planet, Flight
WHERE Planet.id=Flight.planet_id
```

Such way of writing joins used to be popular before the explicit `JOIN` operator came into the standard and was 
implemented in the major SQL engines.

### IN operator makes a join too

What if we want to find the dates of flights to uninhabited planets? We can write this query using joins and filters:

```sql
SELECT flight_date FROM Flight F JOIN Planet P ON F.planet_id=P.id WHERE Planet.is_inhabited = FALSE
```

but if we recall subqueries in `WHERE` clause and `IN` operator, we may rewrite this query as follows:

```sql
SELECT flight_date FROM Flight
WHERE planet_id IN (SELECT id FROM Planet WHERE is_inhabited = FALSE)
```

The subquery returns a list of identifiers of uninhabited planets, and the outer query tests for each row from `Flight` 
if its `planet_id` value is in the list, which is essentially the same as inner join with filter.

We might as well write this query using a cross join and filter:

```sql
SELECT * FROM Planet P, Flight F
WHERE P.id=F.planet_id
  AND P.is_inhabited=false
```

### What way is better?

So, we have a few equivalent ways of writing a query, and one may ask which way is better. The short answer is: 
use explicit join and filters, but there are some aspects to consider:

1. *Code readability and maintainability*. This is the most important aspect, because you write your code for humans. 
A computer doesn't care how clearly you expressed your intents in code. However, it is important that those who read your 
code in the future are able to understand what did you mean. 

From this perspective, if you do a join, use `JOIN`, because it makes the intention clear. Comparing 
to cross-join, where technical join conditions are mixed with important filters in a long `WHERE` clause, `JOIN` allows to 
split them, making clear where is a filter and where is a join. Look at these two queries which both find the planets which
were visited by big spacecrafts built on the shipyards located on the planets with hot climate:

```sql
SELECT P.name
-- This is clearly a chain of joins with join conditions
FROM Planet P JOIN Flight F ON P.id=F.planet_id
JOIN Spacecraft S ON S.id=F.spacecraft_id
JOIN Shipyard Y ON S.shipyard_id=S.id
JOIN Planet P2 ON P2.id=Y.planet_id
-- This is clearly a search filter
WHERE P2.climate = 'hot' AND S.capacity > 10
```

```sql
SELECT P.name
FROM Planet P, Flight F, Spacecraft S, Shipyard Y, Planet P2
-- Here join conditions and a filter are mixed, and it is difficult to distinguish the search filter from 
-- the join conditions
WHERE P.id=F.planet_id 
  AND S.shipyard_id=S.id 
  AND P2.climate = 'hot' 
  AND P2.id=Y.planet_id 
  AND S.id=F.spacecraft_id
  AND S.capacity > 10
```

Besides, outer joins can't be easily expressed with cross-joins and filters. 

Subqueries and `IN` operator may sometimes make a query more understandable, but usually they just add more nested structures which 
makes code less readable. Let's look at the same query written using nested subqueries:

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

There are four levels of nesting and the search filter conditions are apart from each other, and it is difficult to read.
Besides, in the top-level select we are restricted to selecting the attributes of `Planet` table only. We can't select, e.g.,
a flight date along with the planet name, or a spacecraft capacity value.

2. *Performance*. In lots of cases a modern database engine will execute all these queries literally identically, 
no matter if you use an explicit join, `IN` or `CROSS JOIN`, and will use a join algorithm in all cases. This means,
that their performance is likely to be the same. However, it depends on the quality of database query optimization system and some
other factors, such as table sizes and statistics. In general, it is impossible to claim that any of these ways is better than others from
performance perspective.
