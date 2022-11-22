## Subqueries in FROM clause

We know that every SQL query output is a table, and we have seen the usage of scalar and non-scalar subquery outputs 
in `WHERE` and `SELECT`clauses. Can we write a subquery in a `FROM` clause and join its output with other tables?
Yes, sure!   

In the `FROM` clause, we join so-called _table expressions_, that is, expressions that return tables. 
A table name reference is a simple table expression, but more complex expressions are allowed as well, and in particular,
subqueries are also table expressions. 

Let's look at an example. Imagine that we're looking for all spacecraft that had at least two flights in the same year 
but in different months, such that one of the flights was to some inhabited planet and another to an uninhabited one.


```sql
SELECT F.spacecraft_id 
    FROM Flight F JOIN Planet P ON P.id = F.planet_id 
    JOIN (
        --------------------------------------------------------------------------
        -- This subquery returns the flight date, flight number, and flight destination
        -- for flights to inhabited planets.
        SELECT spacecraft_id, flight_date, num, planet_id 
        FROM Flight F2 
        JOIN Planet P2 ON P2.id=F2.planet_id
        WHERE P2.is_inhabited
        ---------------------------------------------------------------------------
        -- It is joined again with Flight and Planet, and we get identifiers of the spacecraft that 
        -- traveled to uninhabited planets in the same year but in different months.
    ) AS T ON (
          -- Notice that we assigned a table alias `T` to the subquery above, 
          -- and we refer to the subquery columns using the `T.` prefix.
          -- We are looking for the same spacecraft
          F.spacecraft_id = T.spacecraft_id 
          -- and flights in the same year 
          AND EXTRACT(YEAR FROM T.flight_date) = EXTRACT(YEAR FROM F.flight_date)
          -- but different months. The standard function EXTRACT returns a specified part from the datetime value.
          AND EXTRACT(MONTH FROM T.flight_date) <> EXTRACT(MONTH FROM F.flight_date)
    )
    WHERE NOT P.is_inhabited
```

 
In this particular case, we could easily write the query without any subqueries:

```sql
  SELECT F.spacecraft_id
  FROM Flight F2 
  JOIN Planet P2     ON P2.id=F2.planet_id
  JOIN Flight F      ON (
      F2.spacecraft_id = F.spacecraft_id 
      AND EXTRACT(YEAR FROM F.flight_date) = EXTRACT(YEAR FROM F2.flight_date)
      AND EXTRACT(MONTH FROM F.flight_date) <> EXTRACT(MONTH FROM F2.flight_date)
  )
  JOIN Planet P  ON P.id = F.planet_id
  WHERE P2.is_inhabited AND NOT P.is_inhabited
```

However, sometimes it may be tricky, inefficient, or just impossible. A very common use case is when we need 
to calculate aggregated values in some groups and then find the maximum or minimum of the calculated
aggregated values. For instance, how do we find the greatest count of flights in the given year across all spacecraft? 
It is easy to count flights by spacecraft and year:


```sql
SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
FROM Flight
GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)
```

And having counted the flights for each spacecraft and year, we can find the maximum using the above query as a subquery:

```sql
SELECT MAX(flight_count) AS max_flight_count
FROM (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)
) AS T 
WHERE flight_year = 2084
```

An interesting follow-up task is to find the spacecraft which set a record and traveled `max_flight_count` times, that is,
the [arg max](https://en.wikipedia.org/wiki/Arg_max).
A classic way of solving this problem is as follows: let `T(spacecraft_id, flight_year, flight_count)` be a table with
the counted flights for each spacecraft. We can find the arg max using this query:

```sql
SELECT * FROM T 
WHERE flight_count = (SELECT MAX(flight_count) FROM T)
```

Now, if we replace `T` with a query that counts the flights, we'll solve the problem:

```sql
SELECT * 
FROM (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)
) AS T
WHERE flight_count = (SELECT MAX(flight_count) FROM (
    SELECT F.spacecraft_id, EXTRACT(YEAR FROM flight_date) AS flight_year, COUNT(*) AS flight_count
    FROM Flight
    GROUP BY spacecraft_id, EXTRACT(YEAR FROM flight_date)    
)) AND flight_year = 2084
```

This is not a piece of cake though because we cloned the body of the query that calculates `T`, which makes maintenance more
difficult and may (but may not as well, depending on the optimizer!) be a performance issue. We'll see how we can make it 
better in the next step.

Finally, one more use case for subqueries. In the query below, we count the flights using different grouping sets – by planet only and 
by planet and spacecraft – and we want to return both aggregated values in the same row with a planet and a spacecraft.
We can't do it using a simple query with `GROUP BY`, so we have to use other means.
One option is grouping by different grouping sets in different subqueries and joining them: 


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
    -- This subquery counts the total number of flights for each planet where we had flights to.  
    SELECT planet_id, COUNT(*) AS planet_flight_count
    FROM Flight 
    GROUP BY planet_id
) AS T1 ON T1.planet_id=P.id
JOIN (
    --------------------------------------------------------------------------------------------
    -- This subquery counts the total number of flights for each pair (planet, spacecraft).  
    SELECT planet_id, spacecraft_id, COUNT(*) AS planet_spacecraft_flight_count
    FROM Flight
    GROUP BY planet_id, spacecraft_id
) AS T2 ON T2.planet_id=P.id
JOIN Spacecraft S ON S.id=T2.spacecraft_id
```

Worth mentioning that in the modern SQL, there are other ways of solving this problem, such as using window functions. 
However, sometimes subqueries are just easier to write and may be more efficient.