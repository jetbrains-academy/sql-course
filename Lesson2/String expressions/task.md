Aside from numerical data, SQL can also operate on other data types. In this step, we
will learn about operations on character data, commonly referred
to as "strings" in modern programming languages.

Programmers with a background in languages like Java, C++, or Python might be
surprised to find that string literals in SQL are enclosed in **single** quotes:

```sql
SELECT 'Hello';
```

In standard SQL, double quotes have a different meaning: they denote _identifiers_, such as
column or table names, rather than string values. While some engines are lenient here – for example, SQLite
accepts `"Hello"` as a string – relying on this behavior is a bad practice. Always use single
quotes for strings.

While most modern programming languages use **`+`** for concatenation, 
the standard SQL concatenation operator is **`||`**:

```sql
SELECT 'Hello, ' || 'World!';
```

Many engines also provide a **`CONCAT`** function for string concatenation. However, because
it is not part of the older SQL standard and is not available in every engine (including the
SQLite version used in this course), we will stick to using `||`.

SQL also includes standard built-in string manipulation functions.
While the exact library varies by engine, core functions
such as **`LOWER`**, **`UPPER`**, and **`SUBSTR`** (also written as `SUBSTRING`) are supported
by nearly all engines:

```sql
SELECT UPPER('hello'), LOWER('HELLO'), SUBSTR('abcdef', 2, 3);
```
