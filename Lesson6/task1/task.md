We know that every SQL query outputs a table, and we have seen how scalar and non-scalar subqueries can be used 
in `WHERE` and `SELECT`clauses. Can you write a subquery in a `FROM` clause and join its result with other tables?
Yes, sure!   

In the `FROM` clause, we join _table expressions_ – expressions that evaluate to tables. 
A standard table reference is a simple table expression, but there are also more complex expressions, and 
subqueries are table expressions as well. 

Let's look at an example. Imagine we want to find all spacecraft that made at least two flights in the same year 
but in different months, such that one flight was to an inhabited planet and the other to an uninhabited one.


```sql
SELECT F.spacecraft_id 
    FROM Flight F JOIN Planet P ON P.id = F.planet_id 
    JOIN (
        --------------------------------------------------------------------------
        -- This subquery returns spacecraft, flight date, flight number, and flight destination
        -- for flights to inhabited planets.
        SELECT spacecraft_id, flight_date, num, planet_id 
        FROM Flight F2 
        JOIN Planet P2 ON P2.id=F2.planet_id
        WHERE P2.is_inhabited
        ---------------------------------------------------------------------------
        -- It is joined again with Flight and Planet, and we get identifiers of the spacecraft that 
        -- traveled to uninhabited planets in the same year but in different months.
    ) AS T ON (
          -- We assigned the table alias `T` to the subquery 
          -- and reference its columns using the `T.` prefix.
          -- We are looking for the same spacecraft
          F.spacecraft_id = T.spacecraft_id 
          -- and flights occurring in the same year 
          AND CAST(strftime('%Y', T.flight_date) AS INTEGER) = CAST(strftime('%Y', F.flight_date) AS INTEGER)
          -- Filter for flights occurring in different months. strftime('%Y', ...) and strftime('%m', ...) return year and month
          -- values as text, so we CAST them to integers for comparison. (Standard SQL uses EXTRACT
          -- for this, but SQLite uses strftime instead.)
          AND CAST(strftime('%m', T.flight_date) AS INTEGER) <> CAST(strftime('%m', F.flight_date) AS INTEGER)
    )
    WHERE NOT P.is_inhabited
```

 
In this particular case, we could easily write the query without subqueries:

```sql
  SELECT F.spacecraft_id
  FROM Flight F2 
  JOIN Planet P2     ON P2.id=F2.planet_id
  JOIN Flight F      ON (
      F2.spacecraft_id = F.spacecraft_id 
      AND CAST(strftime('%Y', F.flight_date) AS INTEGER) = CAST(strftime('%Y', F2.flight_date) AS INTEGER)
      AND CAST(strftime('%m', F.flight_date) AS INTEGER) <> CAST(strftime('%m', F2.flight_date) AS INTEGER)
  )
  JOIN Planet P  ON P.id = F.planet_id
  WHERE P2.is_inhabited AND NOT P.is_inhabited
```

However, doing so without subqueries can be tricky, inefficient, or even impossible. A common use case for subqueries is 
calculating aggregated values for groups and then finding the maximum or minimum across those
aggregations. For instance, how do we find the maximum number of flights made by a single aircraft in a given year? 
It is easy to count flights per spacecraft per year:


```sql
SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
FROM Flight
GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)
```

And having counted the flights for each spacecraft and year, we can find the maximum using the above query as a subquery:

```sql
SELECT MAX(flight_count) AS max_flight_count
FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)
) AS T 
WHERE flight_year = 2121
```

An interesting follow-up task is to find the spacecraft that set a record and flew `max_flight_count` times, that is,
the [arg max](https://en.wikipedia.org/wiki/Arg_max).
A classic approach to solve this problem is as follows: let `T(spacecraft_id, flight_year, flight_count)` be a table containing
the flight counts for each spacecraft per year. We can find the arg max using the following query:

```sql
SELECT * FROM T 
WHERE flight_count = (SELECT MAX(flight_count) FROM T)
```

Now, if we replace `T` with a query that calculates the flight counts, we solve the problem:

```sql
SELECT * 
FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)
) AS T
WHERE flight_count = (SELECT MAX(flight_count) FROM (
    SELECT spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, CAST(strftime('%Y', flight_date) AS INTEGER)    
)) AND flight_year = 2121
```

This approach is far from ideal, however, because we duplicated the subquery logic for `T`. This repetition makes maintenance 
harder and may introduce performance issues (depending on the optimizer). We'll see how to 
improve this in the next step.

Finally, let's look at one more use case for subqueries. In the query below, we count flights across different grouping sets – by planet alone, as 
well as by planet and spacecraft – and we want to return both aggregated values in the same row alongside the planet and spacecraft.
We cannot accomplish this using a standard `GROUP BY` clause, so we must use another approach.
One option is to calculate each grouping set in separate subqueries and join their results: 


```sql
SELECT P.id, 
       P.name, 
       T1.planet_flight_count, 
       S.id, 
       S.name, 
       T2.planet_spacecraft_flight_count
FROM Planet P  
JOIN (
    --------------------------------------------------------------------------------------------
    -- This subquery counts total flights for each planet that has recorded flights.  
    SELECT planet_id, COUNT(*) AS planet_flight_count
    FROM Flight 
    GROUP BY planet_id
) AS T1 ON T1.planet_id=P.id
JOIN (
    --------------------------------------------------------------------------------------------
    -- This subquery counts total flights for each (planet, spacecraft) pair.  
    SELECT planet_id, spacecraft_id, COUNT(*) AS planet_spacecraft_flight_count
    FROM Flight
    GROUP BY planet_id, spacecraft_id
) AS T2 ON T2.planet_id=P.id
JOIN Spacecraft S ON S.id=T2.spacecraft_id
```

It is worth mentioning that in the modern SQL, there are other ways to solve this problem, such as using window functions. 
However, subqueries are sometimes easier to write and can be more efficient in certain scenarios.
