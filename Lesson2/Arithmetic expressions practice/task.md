You've seen how `SELECT` evaluates arithmetic expressions and how parentheses
change the order of operations.

### Task
In `task.sql`, add parentheses to the expression so that the query
returns **42** in the `result` column.

Edit `task.sql` and click **Check**.

### How checking works in this course
When you click **Check**, the course runs the query from your `task.sql` against a small
test database (empty in this task) and compares the rows it returns with the expected ones. If they match, the
task is solved. If they don't, %IDE_NAME% shows a short error message.

For a more detailed report, we recommend clicking the **Show Full Feedback…** link:

<div style="text-align: center; max-width: 350px; margin: 0 auto;">
<img src="images/show_full_report.png" alt="Show Full Feedback link screenshot">
</div>

In the tool window that opens at the bottom of %IDE_NAME%, you'll see something like:

```
The value in 'result' column is wrong. 

EXPECTED: 
| result |
|--------|
| 42     |

ACTUAL: 
| result |
|--------|
| 82     |

QUERY: 
SELECT 8*10 + 4/2 AS result;
```

Read it from the bottom up:

* **QUERY** — the exact SQL that was run, so you can see how your expression was interpreted.
* **ACTUAL** — the table your query actually returned.
* **EXPECTED** — the table the task wants.
* The top line names the problem. Here the `result` value is `82` instead of `42`, because
  without parentheses `/2` applies only to `4` (`8*10 + 4/2` = `80 + 2` = `82`).

Compare **EXPECTED** with **ACTUAL** cell by cell to see what to fix, then click **Check** again —
you can do this as many times as you like.

> **Note.** Above `task.sql` %IDE_NAME% may show a warning like *"No data sources are configured to run
> this SQL and provide advanced code assistance."* You can ignore it in this lesson — the exercises here
> don't need a database connection, and the **Check** button runs the query for you. Connecting a data
> source is explained in the next lesson.
