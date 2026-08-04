Starting with this lesson, most theory tasks ship an `example.sql` file with ready-to-run queries.
You can run them against the database you connected to in one click.
Let's practice with this task's `example.sql`, which has two simple queries.

1. Select the query you want to run, then click the ![](images/run.svg) button or
   press &shortcut:Console.Jpa.Execute; to execute the selected query:
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/exec_query.png" alt="Execute query">
</div>

2. Next, choose the data source to run the query against (select **Use Default Schema**):
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/data_source.png" alt="Data source selection">
</div>   

3. The results open in the **Services** tool window as a data grid — the same grid you saw when browsing a
   table:
<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/result.png" alt="Query results">
</div>  
<br>

### Note
 
> In this task or one of the following tasks, you may see this warning when you open an `.sql` file:
> <div style="text-align: center; max-width: 700px; margin: 0 auto;">
> <img src="images/sql_dialect_warning.png" alt="SQL dialect warning">
> </div>

> Click **Change dialect to…**, select **Project SQL Dialect: SQLite** in the window that appears, and click OK.
> <div style="text-align: center; max-width: 500px; margin: 0 auto;">
> <img src="images/dialect_selection.png" alt="SQL dialect selection">
> </div>

---

**No Database tools?** Run the same queries from the command line: `sqlite3 Lesson3/L3_planet.sqlite`, then paste a
query and press Enter:
<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/sqlite3.png" alt="sqlite3 example">
</div>  


<style>
img {
  display: inline !important;
}
</style>
