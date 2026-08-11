In this exercise, you will write `WHERE` clauses that use **subqueries**.
All tasks operate on a single table named `Planet` with the following columns:

* `id` — Integer identifier of the planet
* `name` — Character string with the name of the planet
* `is_inhabited` — Boolean flag (`1`/`0`) indicating whether the planet is inhabited
* `climate` — Character string describing the planet's climate
  (one of `'cold'`, `'mild'`, `'warm'`, `'hot'`, or `'extremely hot'`)
* `has_weapons` — Boolean flag (`1`/`0`) indicating whether there are dangerous weapons on the planet
* `radius` — Planet's radius

### Task
Read the task definitions in the `task.sql` file and replace the placeholder `true` expression in each
`WHERE` clause with an expression that solves the task:

1. Uninhabited planets whose climate matches the climate of an inhabited planet: Use a subquery with `IN`.
2. Inhabited planets that share a climate with an uninhabited planet: Use a correlated `EXISTS` subquery.
3. Planets whose radius exceeds the average radius: Use a scalar subquery.

Click **Check** at any time to evaluate your work. If a solution fails, you will see a hint to help you fix it.
