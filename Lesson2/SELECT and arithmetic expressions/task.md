In this task, we will write our first SQL code which calculates the value of some
expressions. The minimal executable piece of code in SQL is called a _query_, so we will write SQL queries. These queries will not access or manipulate any tables.

### `SELECT` clause

Every SQL query consists of a few parts, called _clauses_. The simplest query includes
only a _select clause_. It starts with the keyword **`SELECT`** followed by a comma-delimited
list of numeric, string, or other expressions. Here is a query which calculates the result
of an arithmetic expression:

```sql
SELECT (3+4)*6;
```

### Arithmetic expressions

Integer and decimal numbers, arithmetic operations, and parentheses work as you may expect. `2`
is an integer number, `3.14` is a decimal number, `+` is the addition operator, `-` is for subtraction,
`*` is for multiplication, `/` is for division (integer or decimal, depending on the operand type),
`2+3*4` gives `14`, and `(2+3)*4` gives `20`.

There is a number of built-in
standard mathematical functions in SQL. Their full list depends on the SQL engine, that is, on the software
which executes the query; however, the most common functions, such as **`ABS`**, **`SQRT`**, and **`POWER`**, are
available in most, if not all, of the engines.

It is possible to calculate many expressions in a single query, separating them by commas:

```sql
SELECT (3+4)*6, ABS(-273.15), SQRT(1024);
```

You might have noticed semicolons at the end of the queries above. Semicolons indicate the end of the
query and are used for separating different queries from each other. They are not required
per se if there is some other way to mark the query end. However, some clients – programs which
take your queries and send them to the engine – may expect that you explicitly type a semicolon
at the end.

### Output column names

All SQL queries output table-structured results. Even if a query consists of a single SELECT clause
and a single expression, it is still a table with one row and one column
(however, such queries may be treated as scalar values, as we will see in the later lessons). Every column has
a name and a data type, and columns produced by any simple **`SELECT`** query are no exception. If a column
name is not specified, it is chosen by the SQL engine. However, it is possible to specify output column
names explicitly using the keyword `AS`:

```sql
SELECT POWER(2, 2) AS squared, POWER(2, 3) AS cubed, SQRT(2) AS square_root;
```

This query returns one row with the columns `squared`, `cubed`, and `square_root`:

```text
squared | cubed | square_root
--------|-------|-----------------
4.0     | 8.0   | 1.4142135623731
```
