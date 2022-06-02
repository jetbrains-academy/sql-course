## String expressions

Aside from numeric operations, SQL can also operate with other data types. In this step we 
will learn about the operations with character data, or "strings", as they are usually called 
in the modern general-purpose programming languages.

There are a few families of
character data types, but we will get closer to them later. In this lesson we will focus on 
character literals and expressions.

There are some surprises for the programmers with the background in Java/C++/Python and similar languages.
First, string literals in SQL are always quoted with single quotes:

```sql 
SELECT 'Hello';
```

Double quotes may also be used in queries, but for other purposes. We will get back to them later.
If you try to enclose a string literal in double quotes, you may face a weird error:

```sql 
SELECT "Hello";
```

[demo showing the result of using double quotes]

In most modern programming languages and platforms you would use `+` for concatenating two
strings, however in SQL string concatenation operator is `||`. 

```sql 
SELECT 'Hello, ' || 'World!';
```

But despite that `||` has been
in ANSI SQL standard for many years, not all SQL engines support it. There is also a function 
`CONCAT` which concatenates its arguments, and it is supported by nearly all SQL engines:

```sql 
SELECT CONCAT('Hello, ', 'World!');
```

There is a number of other functions for working with strings.
The exact list of them depends on the SQL engine, but the most common functions, 
such as `LOWER`, `UPPER`, `SUBSTRING` are supported by the majority of engines.