Okay, the calculator is nice, but that's obviously not the main point of using SQL for data processing.
One of the important features of spreadsheets is filters, and in this lesson we will learn the SQL way of data filtering.

The queries in this lesson assume that there is a data source, where all the required tables are stored.
Exercise tasks will automatically create a data source – tables and some data – and will
connect your SQL queries to that data source. If you want to run any of these queries elsewhere,
you will need to use your own data source, e.g., a relational database. For your convenience, this
lesson ships an SQLite database file (`L3_planet.sqlite`), shared by all its tasks. The next three steps show
how to open it in %IDE_NAME%'s built-in **Database** tools — connect to it, browse the tables, and run the
example queries; you can also open it with the [SQLite console client](https://www.sqlite.org/cli.html).

We will work with the data of some imaginary space travel company called _Astrofleet_,
which carries passengers and cargo between planets in a galaxy far, far away.
