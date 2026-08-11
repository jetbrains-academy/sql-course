In this exercise, you will practice using **outer joins** on the `Planet` and `Flight` tables:

* `Planet(id, name, is_inhabited, climate, has_weapons, radius)`
* `Flight(num, planet_id, flight_date, spacecraft_id, people_count)` — `planet_id` links a flight to its
  destination planet.

Unlike an inner join, an outer join preserves rows that have no match in the joining table and fills missing
values with `NULL`.

### Task
Read the task definitions in `task.sql` and replace each `true` with an expression that solves the task.

Click **Check** at any time to evaluate your work. If a solution fails, you will see a hint to help you fix it.
