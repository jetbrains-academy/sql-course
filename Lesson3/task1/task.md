The simplest SQL query that selects data from a table looks as follows:

```sql
SELECT * FROM Planet
```

In addition to the `SELECT` clause, we now see a `FROM` clause, which, in its simplest form, consists of a 
single table name. This query selects all rows and all columns from the `Planet` table. The asterisk (`*`) stands for 
"all columns", and all rows are selected because the query includes no filtering criteria.

While such queries may seem dumb, they are frequently used in applications: 
e.g., to populate a drop-down list in a user interface with all available options.

What if a table has many columns, but we only need a few? We can explicitly 
specify the desired column names in the `SELECT` clause:

```sql
SELECT id, name FROM Planet
```

If our data source contains a `Planet` table with `id` and `name` columns, this query will 
select the values of those two columns for each row.

We can also write expressions that reference column values directly. Take a look:

```sql
SELECT 'Planet ' || name FROM Planet
```

You can think of this as the database engine looping through every row in the `Planet` table, 
evaluating the expression `'Planet' || name`  using that row's `name` value,  
and writing the result to the output row.


