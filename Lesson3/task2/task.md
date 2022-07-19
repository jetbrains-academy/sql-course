## Simple data filtering

Queries which select everything from a table are useful, but more often we need to filter out most
of the data and leave only those rows which meet certain criteria. In SQL, we can do that with the 
help of the `WHERE` clause. Let's look at this simple example:

```sql
-- Unless your query is very simple, it makes sense to format it with linebreaks and indents.
-- By the way, line comments in SQL start with a double hyphen.
SELECT id, name 
FROM Planet 
WHERE id=1
```

The above query scans over the `Planet` table and finds the rows where the value of the `id` column
is 1. The `WHERE` clause consists of a logical expression, which returns a boolean value. 
The expressions may be very complex and may use many sophisticated SQL constructs, but very often
it is just a combination of mathematical and logical operators, which work with the column values.

Let's look at a more complex example:
```sql
SELECT id, name 
FROM Planet 
WHERE id=1 OR id=2 OR name='Disa'
```

The expression above evaluates to `true` if the value of the attribute `id` is 1, or if the value of `id` is 2,
or if the value of the `name` attribute is `'Disa'`. Logically, the database engine executes the query as follows:

1. It scans through all the rows of the table specified in the `FROM` clause. 
2. For every row, it evaluates the `WHERE` expression, inserting the attribute values from the current row into the 
   expression as necessary.
3. If the expression evaluates to `true`, the whole current row goes to the `SELECT` clause, which may
process it further, as we saw in the previous lesson.

What are the possible results of evaluation of the `WHERE` expression, except for `true`? 
It may evaluate to `false`, which obviously means that the row will be excluded from the output. 
However, it may as well return `unknown`, or `NULL`, and in this case, the row is also excluded from the output. 

Let's assume that besides `id` and `name`, there is a boolean column `is_inhabited` in the table `Planet` 
and its value is `NULL` if we don't know for sure whether the planet is inhabited. 
Suppose the table looks as follows:

----
| name   | id  | is_inhabited |
|--------|-----|--------------|
| Disa   | 4   | true         |
| Lava   | 2   | false        |
| Reva   | 3   | true         |
| Tibela | 1   | NULL         |

----

What if one of Marsoflot clients is looking for a place to spend their summer vacation off the beaten paths and 
wants to exclude from the search those planets which are certainly inhabited? 
Will the following query return exhaustive results?

```sql
SELECT name FROM Planet WHERE NOT is_inhabited
```

[TODO: demo showing the results of running this query]

Not exactly. It will return only those planets which are certainly not inhabited; however, _Tibela_, which is neither known to be inhabited
nor known to be uninhabited, will be missing because negated `NULL` remains 
`NULL` and the result of the `WHERE` expression is unknown. There are other, more subtle cases where the result set, which
would have been non-empty otherwise, may become empty in the presence of unexpected `NULL` values processed by the `WHERE` clause. 
We will look at such queries in one of the following steps.

[TODO: put a link to a query with NOT IN expression]


