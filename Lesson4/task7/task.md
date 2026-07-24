We can join the result of joining two tables with a third table, and continue joining as needed.
Our database has a `Spacecraft` table:

**Spacecraft**

| id | name      | capacity |
|----|-----------|----------|
| 1  | Falcon 22 | 5        |
| 2  | Falcon 25 | 3        |
| 3  | Falcon 28 | 7        |

The `Flight` table has a `spacecraft_id` column that keeps the identifier of the spacecraft which performed the flight.

Let's look at the result of joining planets and flights:

```sql
SELECT *
FROM Planet P JOIN Flight F ON P.id = F.planet_id
```

| P.name | P.id | P.is_inhabited | F.num | F.planet_id | F.flight_date | F.spacecraft_id |
|--------|------|----------------|-------|-------------|---------------|-----------------|
| Terra  | 1    | 1              | AF201 | 1           | 2122-04-12    | 1               |
| Verda  | 5    | 1              | AF147 | 5           | 2122-05-01    | 3               |
| Verda  | 5    | 1              | AF149 | 5           | 2122-05-08    | 2               |
| Aquara | 2    | 1              | AF210 | 2           | 2122-05-12    | 1               |
| Answer | 42   | 1              | AF305 | 42          | 2122-06-01    | 3               |
| Pyros  | 3    | 0              | AF088 | 3           | 2122-06-15    | 2               |

It is essentially a table with columns and rows, and we can join it with the `Spacecraft` table:

```sql
SELECT *
FROM Planet P JOIN Flight F     ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id
```

(showing only some columns for readability)

| P.name | P.id | F.num | F.spacecraft_id | S.name    | S.capacity |
|--------|------|-------|-----------------|-----------|------------|
| Terra  | 1    | AF201 | 1               | Falcon 22 | 5          |
| Verda  | 5    | AF147 | 3               | Falcon 28 | 7          |
| Verda  | 5    | AF149 | 2               | Falcon 25 | 3          |
| Aquara | 2    | AF210 | 1               | Falcon 22 | 5          |
| Answer | 42   | AF305 | 3               | Falcon 28 | 7          |
| Pyros  | 3    | AF088 | 2               | Falcon 25 | 3          |

Every row in the result joins connected facts from different tables. We can read a row like this:
"Falcon 22, which can carry up to 5 astronauts, flew to the inhabited planet Terra on 2122-04-12".

## Outer join chains

What if we want to add an "outer" part to the results above — that is, always output all the planets, even if
there were no flights to some of them? It might be tempting to write it as follows:

```sql
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
                   JOIN Spacecraft S ON F.spacecraft_id = S.id
```

However, this will not work: the result will be the same as if we used inner joins only. The rows in the outer
part of `Planet LEFT JOIN Flight` have `NULL` in `F.spacecraft_id`, and the comparison `F.spacecraft_id = S.id`
is then `UNKNOWN`, so those rows have no match in the subsequent `JOIN Spacecraft`. If we want to keep the outer
part in a chain of joins, we usually keep using `LEFT JOIN`:

```sql
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
              LEFT JOIN Spacecraft S ON F.spacecraft_id = S.id
```

| P.name  | P.id | F.num | F.spacecraft_id | S.name    | S.capacity |
|---------|------|-------|-----------------|-----------|------------|
| Terra   | 1    | AF201 | 1               | Falcon 22 | 5          |
| Aquara  | 2    | AF210 | 1               | Falcon 22 | 5          |
| Pyros   | 3    | AF088 | 2               | Falcon 25 | 3          |
| Glacia  | 4    | NULL  | NULL            | NULL      | NULL       |
| Verda   | 5    | AF147 | 3               | Falcon 28 | 7          |
| Verda   | 5    | AF149 | 2               | Falcon 25 | 3          |
| Dunar   | 6    | NULL  | NULL            | NULL      | NULL       |
| Solmar  | 7    | NULL  | NULL            | NULL      | NULL       |
| Mirren  | 8    | NULL  | NULL            | NULL      | NULL       |
| Cobar   | 9    | NULL  | NULL            | NULL      | NULL       |
| Frost   | 10   | NULL  | NULL            | NULL      | NULL       |
| Zephyra | 12   | NULL  | NULL            | NULL      | NULL       |
| Answer  | 42   | AF305 | 3               | Falcon 28 | 7          |

Another option is to build the inner join part first and then add the outer part with the planets using `RIGHT JOIN`:

```sql
SELECT *
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id = S.id
        RIGHT JOIN Planet P     ON P.id = F.planet_id
```

Keep in mind, though, that these two approaches are not fully equivalent. If the `Flight` rows have any reason
not to join with `Spacecraft`, we keep them when using a chain of `LEFT JOIN` operators but miss them otherwise.
For instance, if we search for fully booked flights (where `people_count` equals the spacecraft `capacity`) but
still want all planets in the result:

```sql
-- Outputs all planets; for planets with no flights or no fully booked flights, the Flight/Spacecraft columns are NULL.
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
              LEFT JOIN Spacecraft S ON (F.spacecraft_id = S.id AND F.people_count = S.capacity)
```

## Filtering the results of joins

If we have one or more joins in the `FROM` clause, filters in the `WHERE` clause apply to the result of joining.
This allows for writing very powerful queries. For example, which planets did Falcon 25 fly to, and when?

```sql
SELECT P.name AS planet_name, F.flight_date, S.capacity
FROM Planet P JOIN Flight F     ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id
WHERE S.name = 'Falcon 25'
```

| planet_name | flight_date | capacity |
|-------------|-------------|----------|
| Verda       | 2122-05-08  | 3        |
| Pyros       | 2122-06-15  | 3        |

Notice that we used table aliases to keep the query short and to distinguish between the planet and spacecraft
names, which would otherwise both be called `name`.
