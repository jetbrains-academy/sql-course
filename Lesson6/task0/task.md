In this lesson we learn how to use subqueries in the `FROM` clause and common table expressions (CTEs) — two
ways to build a query on top of the result of another query.

We keep working with the _Marsoflot_ data. It is the same as in the previous lesson, but the `Flight` table now
spans **two years** (2121 and 2122), which we will use to group and rank flights. SQLite prints boolean values
as `1`/`0` and a `NULL` as an empty cell. Each exercise ships an SQLite database file (`marsoflot.sqlite`) you
can open with the [SQLite console client](https://www.sqlite.org/cli.html).

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
| 1  | MF201 | 1         | 2122-04-12  | 1             | 5            | ore   |
| 2  | MF147 | 5         | 2122-05-01  | 3             | 4            | NULL  |
| 3  | MF149 | 5         | 2122-05-08  | 2             | 3            | water |
| 4  | MF210 | 2         | 2122-05-12  | 1             | 2            | NULL  |
| 5  | MF305 | 42        | 2122-06-01  | 3             | 7            | seeds |
| 6  | MF088 | 3         | 2122-06-15  | 2             | 1            | ore   |
| 7  | MF412 | 1         | 2122-06-20  | 4             | 8            | tools |
| 8  | MF413 | 1         | 2122-07-02  | 4             | 10           | NULL  |
| 9  | MF414 | 2         | 2122-07-05  | 4             | 6            | water |
| 10 | MF520 | 5         | 2122-07-10  | 1             | 4            | ore   |
| 11 | MF521 | 42        | 2122-07-14  | 2             | 2            | NULL  |
| 12 | MF522 | 3         | 2122-07-20  | 3             | 5            | fuel  |
| 13 | MF530 | 5         | 2122-08-01  | 4             | 9            | tools |
| 14 | MF531 | 1         | 2122-08-03  | 1             | 3            | water |
| 15 | MF101 | 1         | 2121-03-05  | 4             | 7            | ore   |
| 16 | MF102 | 3         | 2121-04-10  | 4             | 5            | fuel  |
| 17 | MF103 | 5         | 2121-05-20  | 4             | 8            | NULL  |
| 18 | MF104 | 2         | 2121-06-12  | 4             | 6            | water |
| 19 | MF105 | 6         | 2121-04-18  | 1             | 3            | ore   |
| 20 | MF106 | 5         | 2121-07-01  | 1             | 4            | seeds |
| 21 | MF107 | 3         | 2121-08-09  | 3             | 2            | NULL  |
| 22 | MF108 | 8         | 2121-09-15  | 4             | 9            | tools |
