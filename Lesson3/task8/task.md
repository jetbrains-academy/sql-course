Every theory task starting with this lesson, we'll usually ship an `example.sql` file with ready-to-run queries. 
You can run them against the database you connected in one click.
Let's practice in it using `example.sql` with two simple queries.

1. Select the request you want to run. When click the ![](images/run.svg) button or 
  press &shortcut:Console.Jpa.Execute; to execute the selected query:
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/exec_query.png" alt="Execute query">
</div>

2. Next, select a data sourse: in which database you want to execute this query (choose "Use Default Schema"):
<div style="text-align: center; max-width: 500px; margin: 0 auto;">
<img src="images/data_source.png" alt="Data source selection">
</div>   

3. The results open in the **Services** tool window as a data grid — the same grid you saw when browsing a
   table:
<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/result.png" alt="Query results">
</div>  


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
