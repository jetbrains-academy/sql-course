Queries that select everything from a table are useful, but more often we need to filter out most
of the data and leave only the rows that meet specific criteria. In SQL, we achieve this using the
help of the `WHERE` clause.

Let's look at a simple example:

```sql
-- Unless your query is very simple, it makes sense to format it with line breaks and indents.
-- Line comments in SQL start with a double hyphen.
SELECT id, name
FROM Planet
WHERE id = 1
```

The above query scans the `Planet` table and finds the rows where the value of the `id` column
is 1. The `WHERE` clause consists of a logical expression that returns a boolean value.
These expressions can be highly complex, using many sophisticated SQL constructs, but they are most often
a combination of mathematical and logical operators applied to column values.

<img src="SQL 101 - Task 3.2 - data filtering.drawio.png" width="827" alt="How the WHERE clause filters rows"/>

Let's look at a more complex example:

```sql
SELECT id, name
FROM Planet
WHERE id = 1 OR id = 2 OR name = 'Pyros'
```

The expression above evaluates to `true` if the value of the `id` attribute is 1, if the value of `id` is 2,
or if the value of the `name` attribute is `'Pyros'`.

----

Logically, the database engine executes the query in three steps:

1. It scans through all the rows of the table specified in the `FROM` clause.
2. For each row, it evaluates the `WHERE` expression, inserting attribute values from the current row into the
   expression as necessary.
3. If the expression evaluates to `true`, the whole current row is passed to the `SELECT` clause for
   further processing, as we saw in the previous lesson.

What happens if the `WHERE` expression evaluates to something other than `true`?
If it evaluates to `false`, the row is obviously excluded from the output.
However, if it evaluates to `NULL` (unknown), the row is also excluded.

Suppose our `Planet` table includes a boolean column `is_inhabited` alongside `id` and `name`, where  
a value of `NULL` indicates that we don't know for sure whether a planet is inhabited.
Remember that SQLite displays `true` as `1`, `false` as  `0`, and `NULL` as an empty cell:

| name  | id | is_inhabited |
|-------|----|--------------|
| Terra | 1  | 1            |
| Pyros | 3  | 0            |
| Verda | 5  | 1            |
| Cobar | 9  |              |


Now suppose an Astrofleet client wants to find a vacation destination off the beaten path and
wants to exclude the planets that are known to be inhabited?
Will the following query give them all available options?

```sql
SELECT name FROM Planet WHERE NOT is_inhabited
```

Not quite. This query will return only planets that are definitely not inhabited (in this table, only _Pyros_).
_Cobar_ (neither known to be inhabited nor uninhabited) will be left out because 
negating `NULL` remains `NULL`, making the `WHERE` expression evaluate to unknown. 
There are other, more subtle cases where unexpected `NULL` values processed by the `WHERE` clause can silently eliminate rows from your result set.
We will take a look at those query scenarios in an upcoming step.
