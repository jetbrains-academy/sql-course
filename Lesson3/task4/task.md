### Subqueries and the IN operator
Let's return to the `IN` operator, which checks if its left-hand operand exists within the set defined by its 
right-hand operand:

```sql
SELECT * FROM Planet
WHERE climate IN ('mild', 'warm')
```

We can explicitly list all elements of the list las shown above, but what if we 
don't know those values when writing the query? For instance, what if we want to find all uninhabited 
planets whose climate matches the climate of al least one inhabited planet? Let's refer to this scenario as the Problem 
hereinafter in this step.

The simplest way to solve this is to build a list for the `IN` operator using a _subquery_. It will look like this:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND climate IN (SELECT climate FROM Planet WHERE is_inhabited)
```

The subquery `SELECT climate FROM Planet WHERE is_inhabited` builds a list of climate values. The outer query then evaluates each row to test
whether its `climate` value appears in the list.

Naturally, the subquery must return a single column whose data type matches the  
left-hand operand. 

### Subqueries and the EXISTS operator

Another useful operator that takes a subquery as an argument is `EXISTS`. 
It returns `true` if the subquery result is not empty.  Consider this basic example:

```sql
SELECT * FROM Planet 
WHERE EXISTS(SELECT climate FROM Planet WHERE is_inhabited)
```

Suppose there are 100 planet records in the table, only one planet is inhabited, and its climate is "mild". 
How many rows will this query return? Right, 100!

This occurs because the SQL engine evaluates the same `EXISTS` expression for each row. 
It always returns `true`, selecting every row from the `Planet` table.

However, `EXISTS` becomes far more useful when a subquery references the values from the outer query's current row. 
Let's look at this:

```sql
SELECT * 
FROM Planet AS P
WHERE NOT is_inhabited AND EXISTS(
    SELECT id FROM Planet WHERE is_inhabited AND climate=P.climate
)
```

This query's `WHERE` clause finds all uninhabited planets for which a matching inhabited planet with the same climate exists. 
Thus, it solves our target Problem. Notice the alias `P` introduced in the `FROM` clause to distinguish between the two references to the 
`Planet` table. 

### Subqueries and the SOME, ANY, and ALL operators

One more case of using subqueries in the `WHERE` clause involves logical operators `ANY` (or `SOME`) and `ALL`. 
These operators compare a scalar value against a list of values item-by-item.
`SOME/ANY` return `true` if at least one comparison evaluates to `true`. `ALL` returns `true` only if every comparison evaluates to `true`.
The list of values to compare can be produced by a subquery. Here's how we can solve our Problem using `ANY`:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND climate = ANY (SELECT climate FROM Planet WHERE is_inhabited)
```

In this query, we use the `=` operator together with `ANY`, and it will evaluate to `true` if the current row's climate matches any climate  
returned by the subquery. This is essentially equivalent to using `IN` with the same subquery.

While `ALL` is rarely used with the `=` operator, it is very useful with inequalities. For instance,
to find uninhabited planets with a radius smaller than every inhabited planet, 
you can write:

```sql
SELECT * FROM Planet
WHERE NOT is_inhabited 
  AND radius < ALL (SELECT radius FROM Planet WHERE is_inhabited)
```

### Compatibility and efficiency
Subqueries alongside `IN` and `EXISTS` are supported across all major SQL engines (though performance 
implementations vary). However, the operators `ALL, ANY, and SOME` are not supported by all engines: e.g.,
SQLite does not support them.

As you have seen, there are many ways of solving the Problem and similar tasks. Some solutions
may be more efficient than others depending on the engine and its version. A solution that is 
fast on one engine might be slow on another. Conversely, 
two completely different solutions might be executed identically under the hood. Learning the subtleties of SQL optimization is beyond 
the scope of this course, but you need to keep these subtleties in mind and always profile performance 
when working with large datasets.
