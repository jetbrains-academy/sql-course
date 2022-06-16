## String expressions

Aside from numerical data, SQL can also operate with other data types. In this step, we 
will learn about the operations with character data, or "strings" as they are usually called 
in modern general-purpose programming languages.

There are a few families of character data types, but we will dive into these details later. 
In this lesson, we will focus on character literals and expressions.

Programmers with a background in Java/C++/Python and similar languages will probably be 
somewhat surprised that string literals in SQL are always marked with single quotes:

```sql 
SELECT 'Hello';
```

Double quotes may also be used in queries, but for other purposes. We will get back to them later.
If you enclose a string literal in double quotes, most likely you will get a weird error:

```sql 
SELECT "Hello";
```

[demo showing the result of using double quotes]

In most modern programming languages and platforms, you would use `+` for concatenating two
strings; however, in SQL, the string concatenation operator is `||`. 

```sql 
SELECT 'Hello, ' || 'World!';
```

But despite the fact that `||` has been
in ANSI SQL standard for many years, not all SQL engines support it. There is also a function 
`CONCAT`, which concatenates its arguments and is supported by nearly all SQL engines:

```sql 
SELECT CONCAT('Hello, ', 'World!');
```

There is a number of other functions for working with strings.
The exact list of them depends on the SQL engine, but the most common functions, 
such as `LOWER`, `UPPER`, and `SUBSTRING`, are supported by the majority of engines.