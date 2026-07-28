In this exercise you reproduce the effect of a join **without** using the `JOIN` operator — first with a
comma (cross join) plus a `WHERE` filter, then with a subquery and the `IN` operator. The tables are:

* `Planet(id, name, is_inhabited, climate, has_weapons, radius)`
* `Flight(num, planet_id, flight_date, spacecraft_id, people_count)` — `planet_id` links a flight to its
  destination planet

### Task
Read the task definitions in `task.sql` and replace each highlighted fragment so the query returns the
required result.

You can click **Check** at any moment. If some solution fails, you will see a hint that may help you fix it.
