In this lesson, you learn how to use subqueries in the `FROM` clause and Common Table Expressions (CTEs) — two
techniques for building a query on top of the result of another query.

We will continue working with the _Astrofleet_ dataset. It is similar to the dataset from the previous lesson, but the `Flight` table now
spans **two years** (2121 and 2122), which we will use to group and rank flights. 
Note: SQLite renders boolean values
as `1` or `0`, and `NULL` values as empty cells. This lesson includes an SQLite database file (`L6_astrofleet.sqlite`),
shared across all tasks, which you can open using the [SQLite console client](https://www.sqlite.org/cli.html).

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

**Spacecraft**

| id | name      | capacity |
|----|-----------|----------|
| 1  | Falcon 22 | 5        |
| 2  | Falcon 25 | 3        |
| 3  | Falcon 28 | 7        |
| 4  | Pegasus   | 12       |
| 5  | Comet 9   | 8        |

**Flight**

| id | num   | planet_id | flight_date | spacecraft_id | people_count | cargo |
|----|-------|-----------|-------------|---------------|--------------|-------|
| 1  | AF201 | 1         | 2122-04-12  | 1             | 5            | ore   |
| 2  | AF147 | 5         | 2122-05-01  | 3             | 4            | NULL  |
| 3  | AF149 | 5         | 2122-05-08  | 2             | 3            | water |
| 4  | AF210 | 2         | 2122-05-12  | 1             | 2            | NULL  |
| 5  | AF305 | 42        | 2122-06-01  | 3             | 7            | seeds |
| 6  | AF088 | 3         | 2122-06-15  | 2             | 1            | ore   |
| 7  | AF412 | 1         | 2122-06-20  | 4             | 8            | tools |
| 8  | AF413 | 1         | 2122-07-02  | 4             | 10           | NULL  |
| 9  | AF414 | 2         | 2122-07-05  | 4             | 6            | water |
| 10 | AF520 | 5         | 2122-07-10  | 1             | 4            | ore   |
| 11 | AF521 | 42        | 2122-07-14  | 2             | 2            | NULL  |
| 12 | AF522 | 3         | 2122-07-20  | 3             | 5            | fuel  |
| 13 | AF530 | 5         | 2122-08-01  | 4             | 9            | tools |
| 14 | AF531 | 1         | 2122-08-03  | 1             | 3            | water |
| 15 | AF101 | 1         | 2121-03-05  | 4             | 7            | ore   |
| 16 | AF102 | 3         | 2121-04-10  | 4             | 5            | fuel  |
| 17 | AF103 | 5         | 2121-05-20  | 4             | 8            | NULL  |
| 18 | AF104 | 2         | 2121-06-12  | 4             | 6            | water |
| 19 | AF105 | 6         | 2121-04-18  | 1             | 3            | ore   |
| 20 | AF106 | 5         | 2121-07-01  | 1             | 4            | seeds |
| 21 | AF107 | 3         | 2121-08-09  | 3             | 2            | NULL  |
| 22 | AF108 | 8         | 2121-09-15  | 4             | 9            | tools |
