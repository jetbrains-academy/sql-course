## Aggregate functions

An aggregate function takes a list of values and returns a single aggregate value for that list. For instance, 
an aggregate value may be the number of elements in the list, or the maximum value, if it makes sense, or the sum
of values for numeric input. The list of supported functions differs across the database engines, but there are five 
standard functions which are supported by all relational databases:

| Function | Description                                                     |
|----------|-----------------------------------------------------------------|
| COUNT    | Counts the number of elements in the input list                 |
| MAX      | Finds the maximum value in the input list                       |
| MIN      | Finds the minimum value in the input list                       |
| SUM      | Finds the sum of values in the input list                       |
| AVG      | Finds the average of values in the input list                   |

Aggregate functions can be used in `SELECT` clause (and also in some other clauses) and it is important to understand
the logical model of calculating them, that it, how do they work from the user perspective.

First, let's look how do we build the list of input values. While it is tempting to write something like `SELECT SUM(1, 2, 3)`,
hoping to get the sum `1+2+3`, in reality things are more complicated. 

Aggregate functions in SQL are parameterized with expressions which build the input list of values. The expressions are 
just like those which are used in `SELECT` clause. They may include column names, and very often they include nothing else
but the column name. When a database engine executes a query with aggregate functions, it first builds all joins in the `FROM`
clause, then filters the result using `WHERE` clause, and after that feeds the result to the aggregate functions. Expressions
written in the aggregate functions are evaluated for each row, and the input list is built from the values produced by 
the expressions.

For instance, if we want to find the maximum radius of all planets, or planets with mild climate, we can do it like this:

```sql
-- This will find the maximum across all planets.
SELECT MAX(radius) FROM Planet;

-- This will find the maximum across planets with mild climate
SELECT MAX(radius) FROM Planet WHERE climate = 'mild';
```

An interesting case is `COUNT` function. It just counts the values in the input, and very often it does not matter what
is counted. The following queries will all return the same result in our database:

```sql
SELECT COUNT(id) FROM Planet;
SELECT COUNT(name) FROM Planet;
SELECT COUNT(42) FROM Planet;
```

Yes, even if the expression is just a constant, the same constant for any row in the `FROM-WHERE` output, `COUNT` will 
count it as many times, as it appears. However, there are some subtle 

## Handling of NULL values

What if there are `NULL` values in the aggregate function input? It may happen if a table column is nullable, 
and there are `NULL` values indeed, or if the expression evaluates to `NULL` because of some other other reasons. 
What if there are _only_ `NULL` values in the input?

All aggregate functions, including `COUNT`, ignore `NULL` values in the input list. If there are `NULL` values in `Planet.climate`
column, the following queries will return different results:

```sql
-- This will return a number which is less than the number of rows, because in some rows the value of "climate" is NULL 
SELECT COUNT(climate) FROM Planet;

-- This will still return the number of rows because the input list will include the same value 42 as many times 
-- as the number of rows in Planet table.
SELECT COUNT(42) FROM Planet;
```

There is a special syntax `COUNT(*)` which means "just count the rows". It is helpful if count the rows is really what
you meant to do (it may not be so, as we will see below). 

All aggregate functions except for `COUNT` return `NULL` if all input values are `NULL` or if the input is empty. 
`COUNT` is the only one standard aggregate function which never returns `NULL`. If the input is empty or if all input values are 
`NULL`, `COUNT` will return `0`.

## Handling of distinct values

By default, the aggregate function input is a list, which allows for duplicates. However, sometimes we may want to 
remove duplicates and calculate distinct values only. 

