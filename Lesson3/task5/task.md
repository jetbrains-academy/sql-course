In this exercise, you have to write `WHERE` clauses that use **subqueries**.
All the tasks operate with a single table named `Planet` with the following columns:

* `id` — the integer identifier of the planet
* `name` — a character string with the name of the planet
* `is_inhabited` — a flag (`1`/`0`) indicating whether the planet is inhabited
* `climate` — a character string describing the planet climate
  (one of `'cold'`, `'mild'`, `'warm'`, `'hot'`, `'extremely hot'`)
* `has_weapons` — a flag (`1`/`0`) indicating if there are dangerous weapons on the planet
* `radius` — the planet radius

### Task
Read the task definitions in the `task.sql` file and replace the trivial `true` expression in each
`WHERE` clause with an expression that solves the task:

1. uninhabited planets whose climate matches the climate of some inhabited planet — use a subquery with `IN`;
2. inhabited planets that share a climate with some uninhabited planet — use a correlated `EXISTS` subquery;
3. planets whose radius exceeds the average radius — use a scalar subquery.

You can click **Check** at any moment. If some solution fails, you will see a hint that may help you fix it.
