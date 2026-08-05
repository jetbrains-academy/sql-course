Boolean logic in general-purpose programming languages is usually two-valued, that is, expressions evaluate to either `true` or `false`. In SQL, however, boolean logic is three-valued. In addition to `true` and `false`, SQL includes **`NULL`**, which stands for an "unknown" value.

Except in a few specific cases, using `NULL` in any comparison or logical operator results in `NULL`. For instance, we don't know if `NULL` is equal to anything, including `NULL` itself:

```sql
-- We don't know if NULL is equal to 0, so the result of this query is NULL:
SELECT NULL = 0;

-- We don't know if NULL equals an empty string, true, false, or even NULL
-- itself. The result of all these expressions is NULL:
SELECT NULL = '', NULL = true, NULL = NULL;
```

The truth tables for the logical operators look as follows.

If one operand of an `AND` expression is `NULL`, the result is `NULL` unless the other operand is `false`.


 **`AND`**   | `false`  | `true`   | `NULL`
---------|----------|----------|-------
 **`false`** | `false`  | `false`  | `false`
 **`true`**  | `false`  | `true`   | `NULL`
 **`NULL`**  | `false`  | `NULL`   | `NULL`

If one operand of an `OR` expression is `NULL`, the result is `NULL` unless the other operand is `true`.

 **`OR`**    | `false` | `true` | `NULL`
-------|-------|------|------
 **`false`** | `false` | `true` | `NULL`
 **`true`**  | `true`  | `true` | `true`
 **`NULL`**  | `NULL`  | `true` | `NULL`

The only way to check whether a value is NULL is using the `IS` and `IS NOT` operators:

```sql
-- The first expression returns true, the second returns false.
SELECT NULL IS NULL, NULL IS NOT NULL;
```

The literal `NULL` is rarely written in expressions, but it frequently appears when selecting rows containing nulls, as we'll see later.
