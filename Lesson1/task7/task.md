You can still use a general-purpose language even if you don't know in advance all the search tasks your application 
needs to perform. Just write and compile new programs on-demand. However, it is a very inefficient waste 
of one of the most valuable resources: software engineer's time. General purpose languages, unfortunately,
are not good at dealing with table-structured data.

There are many other reasons why using your home-brewed storage and querying system, 
such as ones based on reading and writing CSV files with a general purpose language, is not a great idea. However, in this 
course, we will focus only on a query language. We want our queries to be easy to write, read, 
and maintain and to be very expressive to allow for performing complex search tasks over table-structured data.

Well, we are lucky! Such a language exists, and it has been widely used in many software systems for 
some 40+ years. This language is SQL, which stands for Structured Query Language.

Let's throw in a quick example of a query written in SQL:

```sql

SELECT code FROM Airports WHERE country = 'US'
```
This query finds the codes of all airports located in the US. It assumes that somewhere there is a table named `Airports` 
with the columns `code` and `country` and the processor which executes this query knows how to access that table.

SQL is usually associated with complex relational database management systems, which
run on powerful servers in big enterprises. They store and manage table data in their binary format and provide an SQL 
interface for queries. However, there are many other programs which can execute queries written in SQL or an SQL-like
language. For instance, there is a program `trdsql`, which can execute SQL queries over CSV files. An embeddable 
SQL database SQLite can be found in nearly every web browser or Android phone. SQL is used in 
Google's BigQuery data warehouse system, and – surprise! – it is built into Google Sheets, too. Yes, you 
can write SQL-like queries directly in Google Sheets.

[animated gif showing the use of SQL in Google Sheets]

Summarizing, SQL and other similar query languages are everywhere, and knowledge 
of SQL is a great plus for nearly every software engineer. If you're not a software engineer
and you do not write code in your regular work but need to process hundreds or thousands of 
rows in tables, chances are that with SQL you will do it faster.




 