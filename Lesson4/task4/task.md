In this exercise, you will reproduce the effect of a join **without** using the `JOIN` operator — first using a
comma (cross join) with a `WHERE` filter, and then using a subquery and the `IN` operator. The tables are:

* `Planet(id, name, is_inhabited, climate, has_weapons, radius)`
* `Flight(num, planet_id, flight_date, spacecraft_id, people_count)` — `planet_id` links a flight to its
  destination planet

### Task
Read the task definitions in `task.sql` and replace each highlighted fragment so that the query returns the
required result.

Click **Check** at any time to evaluate your work. If a solution fails, you will see a hint to help you fix it.
