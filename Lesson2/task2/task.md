## Boolean expressions

Boolean expressions, which return boolean values, are very important in SQL because 
they are used for data filtering purposes.

There are three boolean values in SQL: `true`, `false`, and `NULL`, which stands for "unknown". Later we will talk more 
about this three-valued logic, but for now, let's assume that we work only with `true` and `false`.


### Comparison operators

Using comparison operators, we can compare values of the same type.

The equality operator in SQL is single `=` (which again may surprise programmers who are used to 
`==` and even `===` operators in other languages), and the inequality symmbol is `<>`, 
like in good old Pascal:

```sql
SELECT 2+2=4, 'war'<>'peace', true=false
```

For many data types, e.g., numeric and character types, the total ordering relationship is 
defined, and their values can be compared with the _less-than_ and similar operators `<`, `<=`, `>`, `>=`:

```sql
SELECT 3*3 < 10, 'foo' > 'bar', 3.14 >= 3.140
```

### Logical operators

Traditional boolean operators `AND`, `OR`, and `NOT` work as expected for `true` and `false` operands.
All the following queries will return `true`:

```sql
SELECT 2+2=4 AND 'war'<>'peace';
SELECT true OR false;
SELECT NOT false;
```

If one of the operands is `NULL`, the result of a boolean operator is also `NULL`, except for these cases: 

```sql
SELECT true OR NULL = true;
SELECT false AND NULL = false;
```

### SQL-specific operators

Some other operators, which are not so common in general-purpose languages, are also widely used in SQL: 

* `<value> BETWEEN <range-start> AND <range-end>` returns `true` if the `value` is in the closed
  range `[range-start, range-end],`
* `<value> IN (<value1>, ..., <valueN>)` returns `true` if the `value` is equal to any of the values 
  in the comma-delimited list in the parentheses.

We will learn some other useful boolean operators in the lesson about filtering. 
