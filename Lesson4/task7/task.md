## Join chains
We can join the results of joining two tables with a third table and continue joining as needed. 
Let's assume that we have a table `Spacecraft` in our database: 

**Spacecraft**

| id            | name      | capacity |
|---------------|-----------|----------|
| 1             | Falcon 22 | 5        |
| 2             | Falcon 25 | 3        |
| 3             | Falcon 28 | 7        |

Let's also assume that there is a column `spacecraft_id` in the `Flight` table, where we keep the identifier of the spacecraft which performed the 
flight. 

Let's look at the result of joining planets and flights:

```sql
SELECT * 
FROM Planet JOIN Flight ON Planet.id=Flight.planet_id
```

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id |
|--------|------|----------------|-------|-------------|---------------|-----------------|
| Disa   | 1    | true           | MF201 | 1           | 2122-04-12    | 1               |
| Reva   | 3    | true           | MF147 | 3           | 2122-05-01    | 3               |
| Reva   | 3    | true           | MF149 | 3           | 2122-05-08    | 2               |
| Disa   | 1    | true           | MF201 | 1           | 2122-05-12    | 1               |

It is essentially a table with columns and rows, and we can join it with the `Spacecraft` table:

```sql
SELECT *
FROM Planet P JOIN Flight F     ON P.id=F.planet_id 
              JOIN Spacecraft S ON F.spacecraft_id=S.id
```

The result will look as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id | S.id | S.name    | S.capacity |
|--------|------|----------------|-------|-------------|---------------|-----------------|------|-----------|------------|
| Disa   | 1    | true           | MF201 | 1           | 2122-04-12    | 1               | 1    | Falcon 22 | 5          |
| Reva   | 3    | true           | MF147 | 3           | 2122-05-01    | 3               | 3    | Falcon 28 | 7          |
| Reva   | 3    | true           | MF149 | 3           | 2122-05-08    | 2               | 2    | Falcon 25 | 3          |
| Disa   | 1    | true           | MF201 | 1           | 2122-05-12    | 1               | 1    | Falcon 22 | 5          | 

Every row in the result joins different connected facts from different tables. 
We can read the contents of every row like this: 
"Falcon 22, which can carry up to 5 astronauts, started its flight to the inhabited planet Disa on 2122-04-12"

## Outer join chains
What if we want to add an "outer" part to the results above, that is, always output all the planets, even if there were no flights
to some of them? The result that we expect looks as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id | S.id | S.name    | S.capacity |
|--------|------|----------------|-------|-------------|---------------|-----------------|------|-----------|------------|
| Disa   | 1    | true           | MF201 | 1           | 2122-04-12    | 1               | 1    | Falcon 22 | 5          |
| Reva   | 3    | true           | MF147 | 3           | 2122-05-01    | 3               | 3    | Falcon 28 | 7          |
| Reva   | 3    | true           | MF149 | 3           | 2122-05-08    | 2               | 2    | Falcon 25 | 3          |
| Disa   | 1    | true           | MF201 | 1           | 2122-05-12    | 1               | 1    | Falcon 22 | 5          | 
| Lava   | 2    | false          | NULL  | NULL        | NULL          | NULL            | NULL | NULL      | NULL       |
| Tibela | 4    | NULL           | NULL  | NULL        | NULL          | NULL            | NULL | NULL      | NULL       |
 
It might be tempting to write such a query as follows:

```sql
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id=F.planet_id
                   JOIN Spacecraft S ON F.spacecraft_id=S.id
```

However, this will not work. If we execute this query, the result will be the same as if we used inner joins only. Why so?
Let's look at the rows produced by the outer join of Planet and Flight and, in particular, at the attributes which 
are involved in the following join condition – `F.spacecraft_id=S.id`:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id |
|--------|------|----------------|-------|-------------|---------------|-----------------|
| Lava   | 2    | false          | NULL  | NULL        | NULL          | NULL            |
| Tibela | 4    | NULL           | NULL  | NULL        | NULL          | NULL            |

The values of `spacecraft_id` in the outer part are always `NULL`, and the comparison with the values of the `Spacecraft.id`
attribute always results to `UNKNOWN`. That is, the rows from the outer part of `Planet LEFT JOIN Flight` have no matching
rows in the subsequent `JOIN Spacecraft`. If we want to keep the outer part in the chain of joins, we usually need to 
continue using `LEFT JOIN`:

```sql
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id=F.planet_id
              LEFT JOIN Spacecraft S ON F.spacecraft_id=S.id
```

Another option is to build the inner join part first and then add the outer part with the planets using `RIGHT JOIN`:

```sql
SELECT *
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id=S.id
        RIGHT JOIN Planet P     ON P.id=F.planet_id
```

Keep in mind, though, that these two approaches are not fully equivalent. 
If the `Flight` rows have any reason not to join with `Spacecraft`, we will keep them in the result when using a chain 
of `LEFT JOIN` operators, but we will miss them otherwise. For instance, let's add again an attribute `Flight.peopple_count`, which 
indicates how many people were onboard, and let's search for fully booked flights where the number of people onboard equals 
the spacecraft capacity. However, we still want all planets to be in the result, even if there were no flights to
some of them, or if there were no fully booked flights. Let's look at the example:

```sql
-- This query will output all the planets. For those planets which had no flights at all or no fully booked flights, 
-- it will output NULL values in the attributes from Flight and Spacecraft tables.
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id=F.planet_id
              LEFT JOIN Spacecraft S ON (F.spacecraft_id=S.id AND F.people_count = S.capacity)

-- This query will output the planets which had no flights but will not output the planets which had no fully booked flights.
SELECT *
FROM Flight F JOIN Spacecraft S   ON (F.spacecraft_id=S.id AND F.people_count = S.capacity)
              RIGHT JOIN Planet P ON P.id=F.planet_id

```


## Filtering the results of joins

If we have one or more joins in the `FROM` clause, filters in the `WHERE` clause will apply to the results of joining. 
This allows for writing very powerful queries. Which planets did Falcon 25 fly to? What were the numbers of the flights to Reva?
Where did we fly to in May 2122? All these queries can  be answered by adding a simple `WHERE` clause to the 
previously joined tables:

```sql
-- This query will find the names of the planets and the flight date of the flights performed by 'Falcon 25'.
SELECT Planet.name AS planet_name, flight_date, capacity
FROM Planet JOIN Flight     ON Planet.id=Flight.planet_id
            JOIN Spacecraft ON Flight.spacecraft_id=Spacecraft.id
WHERE Spacecraft.name = 'Falcon 25'
```

Notice that we had to use table names as column prefixes to distinguish between the planet and spacecraft names.
We can use table aliases to make this shorter:

```sql
SELECT P.name AS planet_name, flight_date, capacity
FROM Planet P JOIN Flight F     ON P.id=F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id=S.id
WHERE S.name = 'Falcon 25'
```
