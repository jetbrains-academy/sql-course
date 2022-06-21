## Filter some rows from a table

Queries which select everything from a table are useful, but more often we need to filter out most
of the data and leave only those rows which meet some criteria. In SQL we can do it with the 
help of `WHERE` clause. Let's look at the simple example:

```sql
-- Unless your query is very simple, it makes sense to format it with linebreaks and indents.
-- By the way, line comments in SQL start with double-dash.
SELECT id, name 
FROM Planet 
WHERE id=1
```

The query above scans over the `Planet` table and finds the rows where the value of `id` column
is 1. The `WHERE` clause consists of a logical expression which returns a boolean value. 
The expressions may be very complex and may use many sophisticated SQL constructs, but very often
it is just a combination of mathematical and logical operators which operate with the column values.

Let's look at more complex example:
```sql
SELECT id, name 
FROM Planet 
WHERE id=1 OR id=2 OR name='Disa'
```

The expression above evaluates to `true` if the value of attribute `id` is 1, or if the value of `id` is 2,
or if the value of `name` attribute is `'Disa'`. Logically, the database engine executes query as follows:

1. It scans through all the rows of the table specified in `FROM` clause 
2. For each single row it evaluates the `WHERE` expression, inserting the attribute values from the current row into the 
   expression as necessary.
3. If the expression evaluates to `true`, the whole current row goes to the `SELECT` clause which may
process it further, as we saw in the previous lesson.

What are the possible results of evaluation of `WHERE` expression, except for `true`? 
It may evaluate to `false`, which obviously means that the row shall be excluded from the output. 
But it may as well return `unknown`, or `NULL`, and in this case the row is excluded from the output either. 

Let's assume that besides `id` and `name` there is a boolean column `is_inhabited` in the table `Planet`, 
and its value is `NULL` if we don't know for sure if the planet is inhabited or not. 
Suppose the table looks as follows:

----
| name   | id  | is_inhabited |
|--------|-----|--------------|
| Disa   | 1   | true         |
| Lava   | 2   | false        |
| Reva   | 3   | true         |
| Tibela | 4   | NULL         |

----

What if one of Marsoflot clients is looking for a place to spend their summer vacation off the crowded beaten paths and 
wants to exclude from the search those planets which are certainly inhabited? 
Will this query return the exhaustive results?

```sql
SELECT name FROM Planet WHERE NOT is_inhabited
```

[demo showing the results of running this query]

Not exactly. There is a row with _Lava_ planet in the results, but _Tibela_ is missing, because negated `NULL` remains 
`NULL`, and the result of `WHERE` expression is unknown. There are other, more subtle cases when a result set, which
would have been non-empty otherwise, may become empty in presence of unexpected `NULL` values processed by the `WHERE` clause. 
We will look at such queries in one of the next steps.


