Aside from numerical data, SQL can also operate with other data types. In this step, we
will learn about the operations with character data, or "strings" as they are usually called
in modern general-purpose programming languages.

Programmers with a background in Java/C++/Python and similar languages will probably be
somewhat surprised that string literals in SQL are marked with **single** quotes:

```sql
SELECT 'Hello';
```

Double quotes have a different meaning in standard SQL: they denote _identifiers_, such as
column or table names, not string values. Some engines are lenient here – for example, SQLite
will treat `"Hello"` as the string `Hello` – but it is a bad habit to rely on that. Use single
quotes for strings.

In most modern programming languages and platforms, you would use **`+`** for concatenating two
strings; however, in SQL, the string concatenation operator is **`||`**:

```sql
SELECT 'Hello, ' || 'World!';
```

Many engines also provide a **`CONCAT`** function that does the same thing. Note, however, that
it is not part of the older SQL standard and is not available in every engine (for instance, the
SQLite version used in this course does not have it), so we will stick to `||`.

There is a number of other functions for working with strings.
The exact list depends on the SQL engine, but the most common functions,
such as **`LOWER`**, **`UPPER`**, and **`SUBSTR`** (also available as `SUBSTRING`), are supported
by the majority of engines:

```sql
SELECT UPPER('hello'), LOWER('HELLO'), SUBSTR('abcdef', 2, 3);
```
