[![official project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Gradle Build](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-build.yml)
[![Gradle Test](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-test.yml/badge.svg)](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-test.yml)
<a href="https://academy.jetbrains.com/course/TODO?=fromGitHub" target="_blank">
<img src="https://img.shields.io/static/v1?logo=jetbrains&logoColor=1bd58e&label=&message=Course%20catalog&color=5a5a5a&style=flat" alt="Course%20catalog"></a>
<a href="https://plugins.jetbrains.com/plugin/TODO/versions" target="_blank">
<img src="https://img.shields.io/badge/dynamic/yaml?query=%24.course_version&url=https://raw.githubusercontent.com/jetbrains-academy/sql-course/refs/heads/master/course-remote-info.yaml&logo=jetbrains&logoColor=FC801D&label=Marketplace&color=6b59fe&style=flat&prefix=v" alt="Marketplace"></a>

> [!WARNING]
> At the moment, to work with this course, the following feature flag must be enabled: `edu.course.sql`

# SQL Introduction
**SQL** (Structured Query Language) is how you talk to databases —
you ask a question, and the database returns the data that answers it. It's one
of the most widely used skills in software engineering and data analysis, and it's wonderfully
approachable: you can write a genuinely useful query on your very first day.

This course is a hands-on introduction to the fundamentals of SQL. Across the lessons,
you'll learn how to:

- Read data with `SELECT` and filter results using `WHERE`
- Work with datatypes, including numbers, text, and boolean expressions
- Combine data from multiple tables using **joins**
- Summarize data with grouping and **aggregate** functions
- Build advanced queries using **subqueries** and **CTEs** (Common Table Expressions)

It's designed for complete beginners — no prior SQL knowledge is assumed. A little
programming experience is helpful, but not required.

## Want to know more?
If you have questions about the course or the tasks, or if you find any errors, 
feel free to ask questions and participate in discussions within the repository's 
[issues](https://github.com/jetbrains-academy/sql-course/issues) section.

## Contributing
Please be sure to review the [project's contributing guidelines](https://github.com/jetbrains-academy/.github/blob/main/contributing_guidelines.md) to learn how you can help improve the project.

## TODO lists
### Content
- [x] Move `astrofleet.sqlite` in each lesson to the lesson-level; update all references to use same DB for all tasks
- [x] Add instructions for learners how to use in-IDE database tool to work with DB in this course
- [x] Add a guide about integrated into IDE CSV-data tools
- [x] Add images for lesson1 (?)
- [x] Add guide about checking system into `Arithmetic expression practice` task
- [x] Rename Lesson2 tasks to use the same prefixes
- [x] Remove Prototype lesson
- [x] Get rid of the appendix lesson
- [x] Check are SQL requests make write to the lessons' .sqlite files → no, the entire curriculum is read-only.
- [x] Update gradle-everything in the Kotlin-onboarding way and add propper files to the repository and course files
- [x] Add intro lesson "how to use the course" -- same as Kotlin Onboarding 1 have
- [x] Test the course by someone available
- [x] Add a link to the EDU licenses page
- [ ] Plugin team -> enable sql course feature flag in prod

### Publication
- [ ] Finalize course name and description
- [ ] Update Readme to match style and links

## Course technical details
### Notes
- SQLite has no boolean type — boolean values are stored and returned as `1`/`0`. The keywords `TRUE`/`FALSE` are accepted in queries (since SQLite 3.23) but are just aliases for `1`/`0`.

### Database Tools and SQL
For a seamless experience, we recommend using the built-in IDE [Database Tools and SQL](https://www.jetbrains.com/pages/intellij-idea-databases/).
This [feature](https://www.jetbrains.com/help/idea/relational-databases.html) requires an Ultimate subscription. 

### SQLite CLI
All steps, including manual database initialization (_not required for the learner anymore_), can be performed via the CLI:
- `sqlite3 test.sqlite < init.sql`
- `sqlite3 test.sqlite < init_data.sql`
- `sqlite3 test.sqlite < src/task.sql`

### Other GUI apps
If learners prefer using another application to inspect the SQLite files within the course, 
the following non-commercial GUI solutions can be used (the list is not exhaustive):
- [DataGrip](https://www.jetbrains.com/datagrip)
- [DB Browser for SQLite (DB4S)](https://sqlitebrowser.org/)

### Tasks architecture
- Learners are provided with a shared sample database for each lesson (`LX_astrofleet.sqlite`).
- Within a lesson, all `example.sql` or `task.sql` can be executed against this database.
- Upon clicking Check, `Tests.kt` builds a fresh `test.sqlite` instance from `init.sql` + `init_data.sql` (inside the task folder -> isolated from the shared DB).
- It runs each query from `task.sql` and compares the rows against the expected result.
- On a mismatch, it shows the Full Feedback available on the **Show Full Feedback…** link.
- `test.sqlite` is visible for the learner – no need to hide it, actually.

```text
LessonX/                       
├── LX_astrofleet.sqlite    // shared sample DB for the lesson  ; visible
└── taskX/                     
    ├── init.sql            // (re)creates tables               ; hidden
    ├── init_data.sql       // (re)fills tables with data       ; hidden
    ├── test.sqlite         // DB created by tests              ; visible
    ├── src
    │   ├── example.sql     // (theory)                         ; visible
    │   └── task.sql        // (practice) user solves task here ; visible
    └── test
        └── Tests.kt                                            ; hidden
```

<img src="images/tasks_architecture.png" alt="Anatomy of an edu task in the new checker architecture"/>

### Full feedback example
```text
org.junit.ComparisonFailure: The query returned an incorrect number of rows 

EXPECTED: 
| id | name   |
|----|--------|
| 3  | Venus  |
| 4  | <null> |

ACTUAL: 
| id | name   |
|----|--------|
| 2  | Uranus |
| 3  | Venus  |
| 4  | <null> |

QUERY: 
SELECT id, name FROM Planet WHERE is_inhabited = false;
```
