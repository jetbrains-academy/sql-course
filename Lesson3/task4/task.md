## Subqueries in WHERE clause

### Subqueries and operator `IN`
Let's return to `IN` operator which checks if its "left-hand" operand is an element of a list defined by its 
"right-hand" operand:

```sql
SELECT * FROM Planet
WHERE climate IN ('mild', 'cool')
```

We can explicitly enumerate all elements of the list, like we did in the query above, but what if we 
don't know them at the moment of writing the query? For instance, what if we want to find all uninhabited 
planets where climate is one of the climate values of inhabited planets? Let's refer to this as The Problem 
hereafter in this step.

The most simple way of solving The Problem is to build a list for `IN` operator using a _subquery_. It will look like this:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND climate IN (SELECT climate FROM Planet WHERE is_inhabited)
```

The subquery `SELECT climate FROM Planet WHERE is_inhabited` builds a list of values, and the outer query for each row will test
if its `climate` value is one of the values produced by the subquery.

Naturally, the subquery is supposed to return a single column with the data type matching the data type 
of the left-hand operand. 

### Subqueries and operator `EXISTS`

Another operator which takes a subquery argument and may render useful in some cases is `EXISTS`. 
It returns `true` if a subquery result is not empty.  Let's start with a dumb example:

```sql
SELECT * FROM Planet 
WHERE EXISTS(SELECT climate FROM Planet WHERE is_inhabited)
```

Let's suppose that there are 100 planet records in the table, and only one of them is inhabited, and its climate is "mild". 
How many rows will be in the result of the whole query?

[demo showing that there will be as many rows as there are planets]

The matter is that during the query execution the SQL engine evaluates the same `EXISTS` expression for each row. 
It always returns `true` and thus we select all rows from the `Planet` table.

However, we can use the values of the outer query current row attributes in a subquery, 
and it makes `EXISTS` operator way more useful. Let's look at this:

```sql
SELECT * 
FROM Planet AS P
WHERE NOT is_inhabited AND EXISTS(
    SELECT id FROM Planet WHERE is_inhabited AND climate=P.climate
)
```

Its `WHERE` clause finds all planets which are not inhabited and for which exists an inhabited planet with the same climate, that
is, it solves The Problem. Notice that we introduced an alias `P` in `FROM` clause to distinguish between two references to 
`Planet` table. 

### Subqueries and operators `SOME`, `ANY`, and `ALL`

One more case when we can use subqueries in `WHERE` clause are logical operators `ANY/SOME` and `ALL`. 
They compare a scalar value with a list of values pair-by-pair using the comparison operator which is passed as the argument.
`SOME/ANY` return `true` if at least one comparison returns `true`. `ALL` returns `true` if all comparisons return `true`.
The list of values to compare can be produced by a subquery. Let's solve The Problem using `ANY`:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND climate = ANY (SELECT climate FROM Planet WHERE is_inhabited)
```

In this query we use `=` operator in pair with `ANY`, and it will evaluate to `true` if any of the climate values 
returned from the subquery matches the current row climate value. This is essentially equivalent to using `IN` with the same subquery.

Operator `ALL` is not that useful with `=`, but it may be useful when used with inequalities. For instance,
if we what to find an uninhabited planet with the radius less than the smallest radius of inhabited planets, 
we can write it like this:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND radius < ALL (SELECT radius FROM Planet WHERE is_inhabited)
```

### Compatibility and efficiency
Subqueries and operators `IN` and `EXISTS` are supported by all major SQL engines, although some of them may not 
implement the subqueries as efficiently as others. Operators `ALL, ANY, SOME` are not supported by some engines, e.g.
they are not supported in SQLite.

As you have seen, there are many ways of solving The Problem and similar tasks. Some solutions
may be more efficient than others depending on the engine and particular versions of the engine. The same solution may be 
efficient when executed by one engine and not efficient when executed by another engine. Vice versa, 
different solutions may be physically executed absolutely identically. Learning the subtleties of SQL optimization is not 
the goal of this course, but you should keep in mind that they may exist, and always run performance profiling 
if you deal with big amounts of data.