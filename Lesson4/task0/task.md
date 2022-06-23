# Joining tables

In relational database systems data are usually located in many tables, and very often for search purposes we need
to do the following: for each row from one table find all rows from another table which meet some criteria, and combine 
the matching pairs of rows. For instance, if we have a table with planets

----
Planets

----

and a table with the flight history

----
Flights

----

we may want to find for each planet row all flight rows with the matching `planet_id` and combine the attributes of 
each matching pair of planet and flight. This way we will find for each planet all flights to that planet.

In this lesson we will learn about the family of `JOIN` operations which are designed for such tasks.

The queries in this lesson assume that there is a data source where all the required tables are stored.
Exercise tasks will automatically create the data source, tables and add some data, and will
connect your SQL queries to the data source. If you want to run any of these queries elsewhere,
you will need to use your own data source, e.g. a relational database. For your convenience, we provide an SQLite database file
which you can use with the [SQLite console client](https://www.sqlite.org/cli.html).

----
**TODO**: provide SQLite file

----

We will work with the data of some imaginary space travel company called _Marsoflot_,
which carries passengers and cargo between planets in a galaxy far, far away. 