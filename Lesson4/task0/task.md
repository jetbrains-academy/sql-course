# Joining tables

Usually, there is more than one table in a relational database, just like there is more than one data sheet in a complex 
spreadsheet. There are good reasons why we need many tables. Leaving aside complicated theory, it is not recommended to 
mix records of different types, wo we'll keep records about planets and flights in different tables:

----
**Planets**

| name   | id        | is_inhabited |
|--------|-----------|--------------|
| Disa   | 1         | true         |
| Lava   | 2         | false        |
| Reva   | 3         | true         |
| Tibela | 4         | NULL         |

----
**Flights**

| num   | planet_id | flight_date |
|-------|-----------|-------------|
| MF201 | 1         | 2122-04-12  |
| MF147 | 3         | 2122-05-01  |
| MF149 | 3         | 2122-05-08  |
| MF201 | 1         | 2122-05-12  |

We use the `planet_id` column to link a flight row with a planet row. If we kept everything in one table, we would 
have to clone planet's data in each flight to that planet, and that could be error-prone and just annoying.

Splitting flights and planets makes our data more healthy, but now when searching, we need to find for each row from one 
table all rows from another table that meet certain criteria and then combine the matching pairs of rows. 
For instance, we may want to find for each planet row all matching flight rows such that `Planet.id = Flight.planet_id` 
and combine the attributes of each matching pair of planet and flight values. 
This way, for each planet, we will find all flights destined to it. 
The expected result will look as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|--------|------|----------------|-------|-------------|---------------|
| Disa   | 1    | true           | MF201 | 1           | 2122-04-12    |
| Reva   | 3    | true           | MF147 | 3           | 2122-05-01    |
| Reva   | 3    | true           | MF149 | 3           | 2122-05-08    |
| Disa   | 1    | true           | MF201 | 1           | 2122-05-12    |

We added table aliases `P` and `F` in the column names to distinguish between the columns from the `Planet` and `Flight`
tables correspondingly. We have four rows in the result because for each flight we have one and only one planet which is
the flight destination. Notice that we have no flight records for _Lava_ and _Tibela_ in our database, so they are
missing in the result.

Such tasks are very common when querying relational data, and there is a family of `JOIN` operations in SQL, 
which are designed for that.
