## Boolean expressions

Boolean expressions, which return boolean values, are very important in SQL, because 
they are used for data filtering purposes.

There are three boolean values in SQL: `true`, `false`, and `NULL` which stands for "unknown". Later we will talk more 
about this three-valued logic, but for now let's assume that we work only with `true` and `false`.


### Comparison operators

We can compare values of the same type using comparison operators.

Equality operator in SQL is single `=` (which again may surprise programmers who are used to 
`==` and even `===` operators in other languages), and inequality is `<>`, 
like in old good Pascal:

```sql
SELECT 2+2=4, 'war'<>'peace', true=false
```

For many data types, e.g. numeric and character types, the total ordering relationship is 
defined, and their values can be compared with less-than and similar operators `<`, `<=`, `>`, `>=`:

```sql
SELECT 3*3 < 10, 'foo' > 'bar', 3.14 >= 3.140
```

### Logical operators

Traditional boolean operators `AND`, `OR`, `NOT` work as expected for `true` and `false` operands.
All the following queries will return `true`:

```sql
SELECT 2+2=4 AND 'war'<>'peace';
SELECT true OR false;
SELECT NOT false;
```

If one of the operands is `NULL` the result of a boolean operator is `NULL` as well, except for these cases: 

```sql
SELECT true OR NULL = true;
SELECT false AND NULL = false;
```

### SQL-specific operators

Some operators which are not so common in general-purpose languages are widely used in SQL: 

* `<value> BETWEEN <range-start> AND <range-end>` returns `true` if the `value` is in the closed
  range `[range-start, range-end]`
* `<value> IN (<value1>, ..., <valueN>)` returns `true` if the `value` equals to any of the values 
  in the comma-delimited list in the parentheses.

We will learn some other useful boolean operators in the lesson about filtering. 
