Now you know how to connect to the database and look at the data without writing a single line of SQL.

The `Planet` table opened in the **data editor** presented as a spreadsheet grid where each 
row is a table row and each column is a table column.

From the grid you can explore the data:

- **Sort** — click a column header; %IDE_NAME% re-runs the query with an `ORDER BY` on that column
  (click again to reverse).
- **Filter** — type a condition into the filter field above the grid, or turn on **Enable Local Filter**
  to get a quick filter on every column.
- **Search** — press &shortcut:Find; (**Find**) to search within the current page of rows.
- **Paging** — if a table has more rows than fit on one page, use the pager buttons to move between pages.
- **View as** — switch between **Table**, **Transpose**, **Tree**, and **Text** views of the same data.

Try sorting planets by `radius` and filtering by `climate` to get a feel for the data before you
start querying it. In later lessons the database has more tables (`Spacecraft`, `Flight`) that you can browse
the same way.

<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/planet_filtered.png" alt="Planet table filtered and sorted">
</div>
