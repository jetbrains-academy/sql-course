## Subqueries in FROM clause

We have seen that a result of joining two tables can be used in subsequent join operations, 
and that a subquery results can be used in `WHERE` clause. Can we join the results of a subquery with other tables?

Yes, we can define a subquery in `FROM` clause and then join the results of running the subquery with other tables 
and subqueries. Let's look at the example. Imagine that we need to find all spacecrafts which started their flight 
at the same date with big spacecrafts to the same planet. 


```sql
SELECT S.name, F.num
    FROM Spacecraft S JOIN Flight F ON S.id = F.spacecraft_id JOIN (
        --------------------------------------------------------------------------
        -- This subquery returns flight date, flight number and flight destination
        -- of big capacity spacecrafts
        SELECT flight_date, num, planet_id 
        FROM Flight F2 
        JOIN Spacecraft S2 ON S2.id=F2.spacecraft_id
        WHERE S2.capacity > 10
        ---------------------------------------------------------------------------
        -- It is joined again with Flight and Spacecraft, and we get flight numbers and names of other spacecrafts 
        -- which started to the same planet at the same date.
    ) AS T ON F.planet_id=T.planet_id AND F.flight_date = T.flight_date AND F.num <> T.num
```

Notice that we assigned a table alias `T` to the subquery, and referred to the subquery columns using `T.` prefix. 

Can we write this query without using subqueries? Yes, in this particular case it is easy:

```sql
  SELECT flight_date, num, planet_id 
  FROM Flight F2 
  JOIN Spacecraft S2 ON S2.id=F2.spacecraft_id
  JOIN Flight F      ON F2.planet_id = F.planet_id AND F2.flight_date = F.flight_date AND F2.num <> F.num
  JOIN Spacecraft S  ON S.id = F.spacecraft_id
  WHERE S2.capacity > 10
```

What's the point of using subqueries then? Well, first they may make a big query more readable, if we extract some complex
or important part and add explanatory comments, as we did in the first example. Second, in many cases writing a complex 
query without subqueries in `FROM` clause is either tricky, or inefficient, or just impossible. 

Let's look at the example below. In this query we count the flights using different grouping sets -- by planet only and 
by planet and spacecraft -- and we want to return both the aggregated values in the same row with a planet and a spacecraft.
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

Worth mentioning that in the modern SQL there are other ways of solving this problem, such as using window functions. 
However, sometimes subqueries are just easier to write and may be more efficient.



