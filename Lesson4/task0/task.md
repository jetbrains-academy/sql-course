Usually, there is more than one table in a relational database, just like there is more than one data sheet
in a complex spreadsheet. There are good reasons why we need many tables. Leaving aside complicated theory,
it is not recommended to mix records of different types, so we keep the records about planets and flights in
different tables. Here is the data we will work with in this lesson (SQLite prints boolean values as `1` and `0`):

**Planet**

| id | name    | is_inhabited | climate       | has_weapons | radius |
|----|---------|--------------|---------------|-------------|--------|
| 1  | Terra   | 1            | mild          | 0           | 6371   |
| 2  | Aquara  | 1            | warm          | 1           | 7000   |
| 3  | Pyros   | 0            | hot           | 1           | 3200   |
| 4  | Glacia  | 0            | cold          | 0           | 5200   |
| 5  | Verda   | 1            | mild          | 1           | 6800   |
| 6  | Dunar   | 0            | warm          | 0           | 4500   |
| 7  | Solmar  | 0            | hot           | 0           | 9800   |
| 8  | Mirren  | 1            | extremely hot | 0           | 8200   |
| 9  | Cobar   | 0            | mild          | 0           | 7100   |
| 10 | Frost   | 0            | cold          | 1           | 5000   |
| 12 | Zephyra | 0            | warm          | 0           | 12000  |
| 42 | Answer  | 1            | mild          | 0           | 4242   |

**Flight**

| num   | planet_id | flight_date | spacecraft_id | people_count |
|-------|-----------|-------------|---------------|--------------|
| MF201 | 1         | 2122-04-12  | 1             | 5            |
| MF147 | 5         | 2122-05-01  | 3             | 4            |
| MF149 | 5         | 2122-05-08  | 2             | 3            |
| MF210 | 2         | 2122-05-12  | 1             | 2            |
| MF305 | 42        | 2122-06-01  | 3             | 7            |
| MF088 | 3         | 2122-06-15  | 2             | 1            |

We use the `planet_id` column to link a flight row with a planet row. If we kept everything in one table, we
would have to clone a planet's data in each flight to that planet, and that could be error-prone and just annoying.

Splitting flights and planets makes our data more healthy, but now when searching, we need to find for each
row from one table all rows from another table that meet certain criteria and then combine the matching pairs of
rows. For instance, we may want to find for each planet row all matching flight rows such that
`Planet.id = Flight.planet_id` and combine the attributes of each matching pair of planet and flight values.
This way, for each planet, we will find all flights destined to it. The expected result will look as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|--------|------|----------------|-------|-------------|---------------|
| Terra  | 1    | 1              | MF201 | 1           | 2122-04-12    |
| Verda  | 5    | 1              | MF147 | 5           | 2122-05-01    |
| Verda  | 5    | 1              | MF149 | 5           | 2122-05-08    |
| Aquara | 2    | 1              | MF210 | 2           | 2122-05-12    |
| Answer | 42   | 1              | MF305 | 42          | 2122-06-01    |
| Pyros  | 3    | 0              | MF088 | 3           | 2122-06-15    |

We added table aliases `P` and `F` in the column names to distinguish between the columns from the `Planet` and
`Flight` tables correspondingly. We have six rows in the result because for each flight we have one and only one
planet which is the flight destination. Notice that the planets with no flights (Glacia, Dunar, Solmar, and
others) are missing from the result.

Such tasks are very common when querying relational data, and there is a family of `JOIN` operations in SQL,
which are designed for that.
