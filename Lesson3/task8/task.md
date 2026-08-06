Starting with this lesson, most theory tasks include an `example.sql` file with ready-to-run queries.
You can execute them against your connected database with a single click.
Let's practice with this task's `example.sql`, which contains two simple queries.

1. Select the query you want to run, then click the ![](images/run.svg) button (or
   press &shortcut:Console.Jpa.Execute;) to execute the selected query:
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/exec_query.png" alt="Execute query">
</div>

2. Next, select the data source to run the query against (choose **Use Default Schema**):
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/data_source.png" alt="Data source selection">
</div>   

3. The query results will open in the **Services** tool window as a data grid — the same grid format used when browsing a
   table:
<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/result.png" alt="Query results">
</div>  


**No Database tools?** You can run the same queries from the command line: `sqlite3 Lesson3/L3_planet.sqlite`, then paste your
query and press Enter:
<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/sqlite3.png" alt="sqlite3 example">
</div>  


<style>
img {
  display: inline !important;
}
</style>
