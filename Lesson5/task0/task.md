# Aggregates and data grouping

Now we can write queries which join a few tables and apply search filter. That's a good start! Now let's proceed 
to more complex tasks.

In this lesson we will first learn how to calculate a scalar aggregate value for a list of rows. For instance, count the 
number of rows, or find a maximum value in a column.

Then we will see how we can group the results of filtering, and calculate aggregate values separately for each group.
For instance, we may group planets with the same climate and calculate the maximum planet radius for each group, or
group and count flights by month.  

The queries in this lesson assume that there is a data source, where all the required tables are stored.
Exercise tasks will automatically create a data source – tables and some data – and will
connect your SQL queries to that data source. If you want to run any of these queries elsewhere,
you will need to use your own data source, e.g., a relational database. For your convenience, we provide an SQLite database file,
which you can use with the [SQLite console client](https://www.sqlite.org/cli.html).

----
**TODO**: provide SQLite file

----

We will work with the data of some imaginary space travel company called _Marsoflot_,
which carries passengers and cargo between planets in a galaxy far, far away. 