## Join chains
We can join the results of joining two tables with a third table and continue joining as needed. 
Let's assume that we have a table `Spacecraft` in our database 

**Spacecraft**

| id            | name      | capacity |
|---------------|-----------|----------|
| 1             | Falcon 22 | 5        |
| 2             | Falcon 25 | 3        |
| 3             | Falcon 28 | 7        |

and a column `spacecraft_id` in the `Flight` table where we keep the identifier of the spacecraft which performed the 
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

It is essentially a table, with columns and rows, and we can join it with `Spacecraft` table:

```sql
SELECT *
FROM Planet JOIN Flight     ON Planet.id=Flight.planet_id 
            JOIN Spacecraft ON Flight.spacecraft_id=Spacecraft.id
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
"Falcon 22, which can carry up to 5 astronauts, started its flight to inhabited planet Disa on 2122-04-12"

## Filtering the results of joins

If we have one or more join in `FROM` clause, filters in `WHERE` clause will apply to the results of joining. 
This allows for writing very powerful queries. Which planets did Falcon 25 fly to? What were the numbers of flights to Reva?
Where did we fly to in May 2122? All these queries can  be answered by adding a simple `WHERE` clause to the 
previously joined tables:

```sql
-- This query will find the names of planets and the flight date of the flights performed by 'Falcon 25'
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

Basically, writing simple search queries comes to determining the tables which need to be joined and adding 
search filters.