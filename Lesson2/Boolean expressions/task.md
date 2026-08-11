Boolean expressions (which return boolean values) are fundamental in SQL because
they drive data filtering.

> **Note.** SQLite, the database engine used in this course, does not have a dedicated boolean data type. It represents
>`true` as  **`1`** and `false` as **`0`** . While the keywords `true` and `false` are accepted, they
> are simply aliases for `1` and `0`.

### Comparison operators

Using comparison operators, you can compare values of the same type.

In SQL, the equality operator is a single **`=`** (which may surprise programmers accustomed to
`==` or `===` in other programming languages). The inequality operator is **`<>`**,
like in good old Pascal:

```sql
SELECT 2+2=4, 'war'<>'peace', true=false;
```

This returns `1`, `1`, `0` (that is, `true`, `true`, `false`).

For many data types (e.g., numeric and character types) the total ordering relationship is
defined, and their values can be compared with the _less-than_ and similar operators (`<`, `<=`, `>`, `>=`):

```sql
SELECT 3*3 < 10, 'foo' > 'bar', 3.14 >= 3.140;
```

### Logical operators

Traditional logical operators `AND`, `OR`, and `NOT` work as expected for boolean operands.
All of the following queries return `1` (true):

```sql
SELECT 2+2=4 AND 'war'<>'peace';
SELECT true OR false;
SELECT NOT false;
```

All of the following queries return `0` (false):

```sql
SELECT 2+2=5 OR 'war'='peace';
SELECT true AND false;
SELECT NOT true;
```

### SQL-specific operators

SQL also provides some special operators, which are not so common in general-purpose languages:

* `<value> BETWEEN <range-start> AND <range-end>` returns `true` if `value` falls within the inclusive
  range `[range-start, range-end]`.
* `<value> IN (<value1>, ..., <valueN>)` returns `true` if `value` matches any value
  in the comma-delimited list in the parentheses.

We will cover additional boolean operators in the upcoming lesson on filtering.
