# Joining tables

Usually there is more than one table in relational databases, and very often when searching we need
for each row from one table find all rows from another table which meet some criteria, and combine 
the matching pairs of rows. For instance, if we have a table with planets

----
**Planets**

| name   | id        | is_inhabited |
|--------|-----------|--------------|
| Disa   | 1         | true         |
| Lava   | 2         | false        |
| Reva   | 3         | true         |
| Tibela | 4         | NULL         |

----

and a table with the flight history records

----
**Flights**

| num   | planet_id | flight_date |
|-------|-----------|-------------|
| MF201 | 1         | 2122-04-12  |
| MF147 | 3         | 2122-05-01  |
| MF149 | 3         | 2122-05-08  |
| MF201 | 1         | 2122-05-12  |

----

we may want to find for each planet row all matching flight rows such that `Planet.id = Flight.planet_id` and combine the attributes of 
each matching pair of planet and flight. This way we will find for each planet all flights to that planet. The expected 
result will look as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|--------|------|----------------|-------|-------------|---------------|
| Disa   | 1    | true           | MF201 | 1           | 2122-04-12    |
| Reva   | 3    | true           | MF147 | 3           | 2122-05-01    |
| Reva   | 3    | true           | MF149 | 3           | 2122-05-08    |
| Disa   | 1    | true           | MF201 | 1           | 2122-05-12    |

We added table aliases `P` and `F` in the column names to distinguish between the columns from `Planet` and `Flight`
tables correspondingly. We have four rows in the result, because for every flight we have one and only one planet which is
the flight destination. Notice that we have no flight records to _Lava_ and _Tibela_ in our database, so they are
missing in the result.

Such tasks are very common when querying relational data, and there is a family of `JOIN` operations in SQL 
which are designed for that.
