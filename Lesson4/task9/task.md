In this exercise you practise **outer joins** on the `Planet` and `Flight` tables:

* `Planet(id, name, is_inhabited, climate, has_weapons, radius)`
* `Flight(num, planet_id, flight_date, spacecraft_id, people_count)` — `planet_id` links a flight to its
  destination planet

Unlike an inner join, an outer join keeps rows that have no match on the other side and fills the missing
columns with `NULL`.

### Task
Read the task definitions in `task.sql` and replace each `true` with an expression that solves the task.

You can click **Check** at any moment. If some solution fails, you will see a hint that may help you fix it.
