Using SQL as a calculator is neat, but that's obviously not the primary reason we use it for data processing.
One of the key features of any spreadsheet tool is data filtering, and in this lesson, we will learn how to filter data the SQL way.

The queries in this lesson assume access to a data source containing the required tables.
Exercise tasks will automatically set up this data source – creating tables and populating them with data – and 
connect your SQL queries to it. If you want to run any of these queries outside the task environment,
you will need to connect to your own data source, e.g., a relational database. For your convenience, this
lesson includes an SQLite database file (`L3_planet.sqlite`), which is shared across all tasks in this lesson. The next three steps show
how to open this file in %IDE_NAME%'s built-in [**Database** tools](https://www.jetbrains.com/help/idea/relational-databases.html) (connecting to the database, browsing its tables, and running
example queries). Alternatively, you can open the file using the [SQLite console client](https://www.sqlite.org/cli.html).

Throughout this lesson, we will work with data from an imaginary space-travel company called _Astrofleet_,
which carries passengers and cargo between planets in a galaxy far, far away.
