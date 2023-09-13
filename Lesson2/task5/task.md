## Three-valued logic

----

Boolean logic in general-purpose programming languages is usually two-valued [TODO: check], that is, there are two boolean values: `true` and `false`. In SQL boolean logic is three-valued. In addition to `true` and `false` there is **`NULL`**, which stands for "unknown".

Except for some cases, the result of using `NULL` in any comparison or logical operator is also `NULL`. For instance, we don't know if `NULL` equals to anything, including `NULL` itself:

```sql
-- We don't know if NULL is equal to 0 or not, so the result of this query is NULL:
SELECT NULL = 0;

-- We don't know if NULL equals to empty string, true or false, and even to NULL
-- itself. The result of all these expressions is NULL:
SELECT NULL = '', NULL = true, NULL = NULL;
```

The truth matrixes [TODO: check terminology] of the logical operators look as follows.

If one of the logical `AND` operands is `NULL`, the result is `NULL` unless another operand is `false`.


 **`AND`**   | `false`  | `true`   | `NULL`  
---------|----------|----------|-------
 **`false`** | `false`  | `false`  | `false` 
 **`true`**  | `false`  | `true`   | `NULL`  
 **`NULL`**  | `false`  | `NULL`   | `NULL`  

If one of the logical `OR` operands is `NULL`, the result is `NULL` unless another operand is `true`.

 **`OR`**    | `false` | `true` | `NULL` 
-------|-------|------|------
 **`false`** | `false` | `true` | `NULL` 
 **`true`**  | `true`  | `true` | `true` 
 **`NULL`**  | `NULL`  | `true` | `NULL` 

The only way to check if some value is NULL is using operators `IS` and `IS NOT`:

```sql
-- The first expression returns true, the second returns false.
SELECT NULL IS NULL, NULL IS NOT NULL
```

`NULL` value is rarely used in expressions as a literal, however, it may appear when we select data rows containing nulls from a table, as we'll see later.