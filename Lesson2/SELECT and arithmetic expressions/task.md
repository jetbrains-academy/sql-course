In this task, we will write our first SQL code to calculate the value of several
expressions. The smallest executable unit of code in SQL is called a _query_. The queries we write here will not access or modify any database tables.

### SELECT clause

Every SQL query consists of structural components called _clauses_. The simplest possible query contains
only a _select clause_. It starts with the keyword **`SELECT`**, followed by a comma-delimited
list of numeric, string, or other expressions. Here is a query that calculates the result
of an arithmetic expression:

```sql
SELECT (3+4)*6;
```

### Arithmetic expressions

Integer and decimal numbers, arithmetic operations, and parentheses behave as you may expect. `2`
is an integer, `3.14` is a decimal number, `+` is the addition operator, `-` is for subtraction,
`*` is for multiplication, and `/` is for division (integer or decimal, depending on operand types).
`2+3*4` gives `14`, and `(2+3)*4` gives `20`.

SQL also includes built-in
standard mathematical functions. Their full list depends on the SQL engine, that is, on the software
executing the query; however, the most common functions, such as **`ABS`**, **`SQRT`**, and **`POWER`**, are
universally available across almost all of the engines.

You can calculate multiple expressions within a single query by separating them with commas:

```sql
SELECT (3+4)*6, ABS(-273.15), SQRT(1024);
```

You may have noticed semicolons at the end of the queries above. Semicolons mark the end of an
SQL query and are used to separate multiple queries. They are not strictly required
if there is some other way to mark the query end. However, some clients – programs that
take your queries and send them to the engine – may expect you to explicitly type a semicolon
at the end.

### Output column names

All SQL queries output table-structured results. Even a simple SELECT query
returning a single value produces a table with one row and one column
(however, such queries may be treated as scalar values, as we will see in further lessons). Every column has
a name and a data type, and columns produced by a simple **`SELECT`** query are no exception. If you don't provide a column
name, the SQL engine assigns a default name automatically. You can explicitly name your output columns
using the `AS` keyword:

```sql
SELECT POWER(2, 2) AS squared, POWER(2, 3) AS cubed, SQRT(2) AS square_root;
```

This query returns one row with three columns named `squared`, `cubed`, and `square_root`:

```text
squared | cubed | square_root
--------|-------|-----------------
4.0     | 8.0   | 1.4142135623731
```
