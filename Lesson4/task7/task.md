We can join the result of joining two tables with a third table, and continue chaining additional tables as needed.
Our database contains a `Spacecraft` table:

**Spacecraft**

| id | name      | capacity |
|----|-----------|----------|
| 1  | Falcon 22 | 5        |
| 2  | Falcon 25 | 3        |
| 3  | Falcon 28 | 7        |

The `Flight` table includes a `spacecraft_id` column that flight each flight to the spacecraft that performed it.

Recall the result of joining planets and flights:

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

It is essentially a table with columns and rows, and we can join it directly with the `Spacecraft` table:

```sql
SELECT *
FROM Planet P JOIN Flight F     ON P.id = F.planet_id
              JOIN Spacecraft S ON F.spacecraft_id = S.id
```

(Selected columns shown for readability)

| P.name | P.id | F.num | F.spacecraft_id | S.name    | S.capacity |
|--------|------|-------|-----------------|-----------|------------|
| Terra  | 1    | AF201 | 1               | Falcon 22 | 5          |
| Verda  | 5    | AF147 | 3               | Falcon 28 | 7          |
| Verda  | 5    | AF149 | 2               | Falcon 25 | 3          |
| Aquara | 2    | AF210 | 1               | Falcon 22 | 5          |
| Answer | 42   | AF305 | 3               | Falcon 28 | 7          |
| Pyros  | 3    | AF088 | 2               | Falcon 25 | 3          |

Each row in the result combines connected facts across all three tables. For example, the first row reads:
"Falcon 22, which can carry up to 5 astronauts, flew to the inhabited planet Terra on 2122-04-12."

## Outer join chains

What if we want to include an "outer" part in the results above — that is, always output all planets, even if
some have no flights? It might be tempting to write it as follows:

```sql
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
                   JOIN Spacecraft S ON F.spacecraft_id = S.id
```

However, this won't work: the result will be identical to an inner join. Rows in the outer
part of `Planet LEFT JOIN Flight` contain `NULL` for `F.spacecraft_id`. Consequently, the comparison `F.spacecraft_id = S.id`
evaluates to `UNKNOWN`, leaving those rows without a match in the subsequent `JOIN Spacecraft`. To preserve the outer
part across a chain of joins, you must continue using `LEFT JOIN` throughout the sequence:

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

Another option is to build the inner join part first and then add the outer part for planets using a `RIGHT JOIN`:

```sql
SELECT *
FROM Flight F JOIN Spacecraft S ON F.spacecraft_id = S.id
        RIGHT JOIN Planet P     ON P.id = F.planet_id
```

Keep in mind, though, that these two approaches are not entirely equivalent. If any `Flight` rows
fail to match with `Spacecraft`, a chain of `LEFT JOIN`s will preserve them.
For instance, consider searching for fully booked flights (where `people_count` equals spacecraft `capacity`) while
still including all planets in the result:

```sql
-- Includes all planets; for planets with no flights or no fully booked flights, the Flight/Spacecraft columns are NULL.
SELECT *
FROM Planet P LEFT JOIN Flight F     ON P.id = F.planet_id
              LEFT JOIN Spacecraft S ON (F.spacecraft_id = S.id AND F.people_count = S.capacity)
```

## Filtering the results of joins

If a `FROM` clause contains one or more joins, any filters in the `WHERE` clause apply to the final joined result.
This allows you to write very powerful queries. For example, which planets did Falcon 25 fly to, and when?

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

Notice that we used table aliases to keep the query concise and to distinguish between planet and spacecraft
names, which would otherwise share the column name `name`.
