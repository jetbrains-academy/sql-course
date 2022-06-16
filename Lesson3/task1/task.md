## Select all rows from a table

The most simple SQL query which selects data from a table looks as follows:

```sql
SELECT * FROM Planet
```

It selects all rows and all columns from the table named `Planet`. A star symbol `*` stands for 
"all columns", while all rows are selected just because there is no filter in the query.

Such queries may appear dumb, but they are used in many applications indeed, 
e.g. to fill in a drop-down list in the UI with all available values.

What if there are many columns in the table, and we need only a few of them? We can explicitly 
enumerate the columns which we need in `SELECT` clause:

```sql
SELECT id, name FROM Planet
```

If we have a table `Planet` with the columns `id` and `name` in the data source, this query will 
select the values of those columns for each row.

We can write expressions which use the values from columns. Let's look at this:

```sql
SELECT 'Planet ' || name FROM Planet
```

You may think that the database engine loops over all rows of the table `Planet` and for each row
inserts the value of the attribute `name` from that row into the expression 
`'Planet' || name` and writes the result as the output row.


