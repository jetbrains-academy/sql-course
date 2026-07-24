In the previous lessons, we learned how to write queries with joins and search filters, and that's a good start!
Now let's proceed to more complex tasks and talk about the SQL way of doing something similar to pivot tables in spreadsheets.

We'll start with calculating a scalar aggregate value over a list of rows:
for instance, counting the number of rows or finding the maximum value in a column.

Then we will see how we can group the results of filtering and calculate aggregate values separately for each group.
As an example, we may group planets with the same climate and calculate the maximum planet radius for each group, or
group and count flights by spacecraft.

We keep working with the data of the imaginary space travel company _Astrofleet_. This lesson uses a slightly
richer dataset than before — the `Flight` table now has an `id` and a `cargo` column, there are more flights,
and there is a spacecraft that never flew. This lesson ships an SQLite database file (`L5_astrofleet.sqlite`),
shared by all its tasks, that you can open with the [SQLite console client](https://www.sqlite.org/cli.html). SQLite prints boolean values
as `1` and `0`, and a `NULL` as an empty cell.

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

| id | num   | planet_id | flight_date | spacecraft_id | people_count | cargo  |
|----|-------|-----------|-------------|---------------|--------------|--------|
| 1  | AF201 | 1         | 2122-04-12  | 1             | 5            | ore    |
| 2  | AF147 | 5         | 2122-05-01  | 3             | 4            | NULL   |
| 3  | AF149 | 5         | 2122-05-08  | 2             | 3            | water  |
| 4  | AF210 | 2         | 2122-05-12  | 1             | 2            | NULL   |
| 5  | AF305 | 42        | 2122-06-01  | 3             | 7            | seeds  |
| 6  | AF088 | 3         | 2122-06-15  | 2             | 1            | ore    |
| 7  | AF412 | 1         | 2122-06-20  | 4             | 8            | tools  |
| 8  | AF413 | 1         | 2122-07-02  | 4             | 10           | NULL   |
| 9  | AF414 | 2         | 2122-07-05  | 4             | 6            | water  |
| 10 | AF520 | 5         | 2122-07-10  | 1             | 4            | ore    |
| 11 | AF521 | 42        | 2122-07-14  | 2             | 2            | NULL   |
| 12 | AF522 | 3         | 2122-07-20  | 3             | 5            | fuel   |
| 13 | AF530 | 5         | 2122-08-01  | 4             | 9            | tools  |
| 14 | AF531 | 1         | 2122-08-03  | 1             | 3            | water  |

Note that the spacecraft _Comet 9_ has no flights, _Pegasus_ flew to _Terra_ twice, and some flights carry no cargo.
