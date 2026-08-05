You can still use a general-purpose language even if you don't know all of your application's search   
requiements in advance. You could simply write and compile new programs on demand. However, this is an inefficient use 
of one of a software engineer's time. General-purpose languages, unfortunately,
aren't designed to deal with table-structured data.

There are many reasons why building your own storage and querying system 
(such as parsing CSV files with Kotlin or Python) is not a great idea. In this course, however, 
we will focus specifically on query languages. We want our queries to be easy to write, read, 
and maintain, while remaining expressive enough to handle complex operations over table-structured data.

Fortunately, such a language already exists and has been widely used across software systems for 
over 40 years. This is **SQL**, which stands for **Structured Query Language**. 

SQL is pronounced either by spelling out the letters (*S-Q-L*) or as the word *sequel*.

Here is a quick example of a query written in SQL:

```sql
SELECT code FROM Airports WHERE country = 'US'
```
This query finds the codes of all airports located in the US. It assumes that a table named **`Airports`** 
exists with **`code`** and **`country`** columns, and that the processor executing this query knows how to access that table.

SQL is often associated with enterprise relational database management systems 
running on powerful servers. These systems store and manage table data in proprietary binary formats and provide an SQL 
interface for running queries.

However, many other programs can execute SQL or SQL-like queries.
For instance, the **`trdsql`** tool lets you execute SQL directly against CSV files. An embeddable 
SQL database **[SQLite](https://sqlite.org)** is built into nearly every modern web browser and Android phone. SQL is also used in 
Google's [BigQuery](https://cloud.google.com/bigquery) data warehouse system, and it's even built into Google Sheets! Yes, you 
can write SQL-like queries directly in Google Sheets.

In summary, SQL and SQL-like query languages are everywhere. Knowing 
SQL is a huge asset for software engineers. And if you aren't a developer
but frequently work with thousands of 
rows of data, SQL will help you get your job done much faster.
