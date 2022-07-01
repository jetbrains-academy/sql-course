## Join chains
We can join the results of one join with a third table and continue joining as needed. Let's assume that we have a 
table `Spacecraft` in our database 

and a column `spacecraft_id` in the `Flight` table where we write the identifier of the spacecraft which performed the 
flight. 

Let's look at the result of joining planets and flights:

```sql
SELECT * 
FROM Planet JOIN Flight USING(planet_id)
```

| P.name | P.planet_id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id |
|--------|-------------|----------------|-------|-------------|---------------|-----------------|
| Disa   | 1           | true           | MF201 | 1           | 2122-04-12    | 1               |
| Reva   | 3           | true           | MF147 | 3           | 2122-05-01    | 3               |
| Reva   | 3           | true           | MF149 | 3           | 2122-05-08    | 2               |
| Disa   | 1           | true           | MF201 | 1           | 2122-05-12    | 1               |

It is essentially a table, with columns and rows, and we can join it with `Spacecraft` table:

```sql
SELECT *
FROM Planet JOIN Flight USING(planet_id) JOIN Spacecraft USING(spacecraft_id)
```


## Filtering the results of joins
