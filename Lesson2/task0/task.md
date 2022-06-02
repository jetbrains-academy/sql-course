# Using SQL as a calculator

In this lesson we will write the first SQL queries which calculate the values of some 
expressions. These queries will not access or touch any tables.

## `SELECT` clause

Every SQL query consists of a few parts, called _clauses_. The simplest query includes 
only _select-clause_. It starts with the keyword `SELECT` followed by a comma-delimited 
list of numeric, string or other expressions. Here is a query which calculates the result 
of an arithmetical expression. 

```sql
SELECT (3+4)*6;
```

## Arithmetical expressions

Integer and decimal numbers, arithmetical operations and parentheses work as you may expect. `2`
is an integer number, `3.14` is a decimal number, `+` is the addition operator, `-` is the subtraction,
`*` is the multiplication, `/` is the division (integer or decimal depending on the operand types),
`2+3*4` produces `14`, `(2+3)*4` produces `20`.

There is a number of built-in 
standard mathematical functions. Their full list depends on the SQL engine, that is, on the software 
which executes the query, however, the most common functions, such as `ABS`, `SQRT`, `LOG` are 
available in the most, if not all, of the engines. 

It is possible to calculate many expressions in a single query, separating them by commas:

```sql
SELECT (3+4)*6, ABS(−273.15), SQRT(1024);
```

You might have noticed semicolons at the end of the queries above. Semicolons indicate the end of the 
query, and are used for separating different queries from each other. They are not required 
per se, if there is some other way to mark the query end, however, some clients -- programs which 
take your queries and send them to the engine -- may expect that you explicitly type a semicolon
at the end.

[demo of psql console goes here]

## Output column names

All SQL queries output table-structured results. Even if a query consists of a single select-clause
and a single expression, it is still a table with one row and one column. Every column has
a name and a data type, and columns produced by simple `SELECT`s are no exception. If a column
name is not specified, it is chosen by the SQL engine. However, it is possible to specify output column
names explicitly using the keyword `AS`:

```sql
SELECT POWER(2, 2) AS squared, POWER(2, 3) AS cubed, SQRT(2) AS square_root
```

[the result of running this query]