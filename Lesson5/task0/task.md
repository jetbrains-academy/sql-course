# Grouping data

In this lesson first we will learn how to calculate a scalar aggregate value for a list of rows. For instance, count the 
number of rows, or find a maximum value in a column.

Then we will see how can we group the results of filtering, and calculate aggregate values separately for each group.
For instance, we will see how to calculate the maximum planet radius for each group of planet with the same climate.


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