Relational database usually contain multiple tables, much like 
a complex spreadsheet containing multiple sheets. There are good reasons why we need many tables. Leaving aside complicated theory,
a simple rule of thumb is to avoid mixing records of different types in a single table. Therefore, we keep records about planets and records about flights in
separate tables. Here is the dataset we will work with in this lesson (remember that SQLite represents boolean values as `1` and `0`):

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
| AF201 | 1         | 2122-04-12  | 1             | 5            |
| AF147 | 5         | 2122-05-01  | 3             | 4            |
| AF149 | 5         | 2122-05-08  | 2             | 3            |
| AF210 | 2         | 2122-05-12  | 1             | 2            |
| AF305 | 42        | 2122-06-01  | 3             | 7            |
| AF088 | 3         | 2122-06-15  | 2             | 1            |

We use the `planet_id` column in `Flight` to link a flight row with its corresponding planet row in `Planet`. If we kept everything in one table, we
would have to clone a planet's details for each flight heading there, which is repetitive and error-prone.

Splitting flights and planets keeps our database clean. However, when searching, we often need to match
rows across tables based on common criteria and combine their 
attributes. For instance, we may want to match each planet row with all corresponding flight rows where
`Planet.id = Flight.planet_id`. Combining attributes from matching pairs gives us
all flights destined for each planet. The expected result will look as follows:

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date |
|--------|------|----------------|-------|-------------|---------------|
| Terra  | 1    | 1              | AF201 | 1           | 2122-04-12    |
| Verda  | 5    | 1              | AF147 | 5           | 2122-05-01    |
| Verda  | 5    | 1              | AF149 | 5           | 2122-05-08    |
| Aquara | 2    | 1              | AF210 | 2           | 2122-05-12    |
| Answer | 42   | 1              | AF305 | 42          | 2122-06-01    |
| Pyros  | 3    | 0              | AF088 | 3           | 2122-06-15    |

Here, we used table aliases `P` (`Planet`) and `F` (`Flight`) in the column names to distinguish between columns from each
table. The result contains six rows because each flight has exactly one 
destination planet. Notice that the planets with no flights (Glacia, Dunar, Solmar, 
etc.) do not appear in this result.

Such tasks are very common when querying relational data, and SQL provides a family of `JOIN` operations 
specifically designed for this purpose.
