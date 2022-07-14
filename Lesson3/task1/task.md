## Select all rows from a table

The most simple SQL query which selects data from a table looks as follows:

```sql
SELECT * FROM Planet
```

In addition to the `SELECT` clause, we see the `FROM` clause, which, in its simplest form, consists of a 
single table name. This query selects all rows and all columns from the table named `Planet`. The star symbol `*` stands for 
"all columns", while all rows are selected just because there is no filter in the query.

Such queries may appear dumb, but they are actually used in many applications: 
e.g., to fill in a drop-down list in the user interface with all available values.

What if there are many columns in the table and we need only some of them? We can explicitly 
specify the columns that we need in the `SELECT` clause:

```sql
SELECT id, name FROM Planet
```

If our data source has a table `Planet` with the columns `id` and `name`, this query will 
select the values of those columns for each row.

We can write expressions which use the values from columns. Let's look at this:

```sql
SELECT 'Planet ' || name FROM Planet
```

You may think that the database engine loops over all the rows of the table `Planet`, 
inserts the value of the attribute `name` from each row into the expression 
`'Planet' || name`, and writes the result as the output row.


