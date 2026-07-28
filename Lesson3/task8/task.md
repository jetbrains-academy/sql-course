Every theory task in this course ships an `Main.kt` file with ready-to-run queries. Here is how to run
them against the database you connected. This task ships its own `Main.kt` with two simple queries —
open it and try the steps below.

1. Open a `.sql` file (for example, this task's `Main.kt`).
2. Put the caret inside the statement you want to run — or **select** just a fragment. When you select code,
   %IDE_NAME% "executes only the selection", so you can run a single query out of many.
3. Click the green **Execute** (Run) icon on the left of the line (or in the toolbar), or press **Ctrl+Enter**.
4. If %IDE_NAME% asks, choose **the statement under the caret** or **all statements**. The first time, if the
   file is not attached to a data source yet, pick the one you created from the list.
5. The results open in the **Services** tool window as a data grid — the same grid you saw when browsing a
   table.

**Tip — the query console.** Select the data source in the **Database** tool window and press **F4** to open a
**query console**: a scratch SQL file already connected to the database. Type a query, press **Ctrl+Enter**,
and it runs immediately — much like a terminal.

**No Database tools?** Run the same queries from the command line: `sqlite3 L3_planet.sqlite`, then paste a
query and press Enter (see Lesson 1).
