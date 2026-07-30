[![official project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Gradle Build](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-build.yml)
[![Gradle Test](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-test.yml/badge.svg)](https://github.com/jetbrains-academy/sql-course/actions/workflows/gradle-test.yml)
<a href="https://academy.jetbrains.com/course/TODO?=fromGitHub" target="_blank">
<img src="https://img.shields.io/static/v1?logo=jetbrains&logoColor=1bd58e&label=&message=Course%20catalog&color=5a5a5a&style=flat" alt="Course%20catalog"></a>
<a href="https://plugins.jetbrains.com/plugin/TODO/versions" target="_blank">
<img src="https://img.shields.io/badge/dynamic/yaml?query=%24.course_version&url=https://raw.githubusercontent.com/jetbrains-academy/sql-course/refs/heads/master/course-remote-info.yaml&logo=jetbrains&logoColor=FC801D&label=Marketplace&color=6b59fe&style=flat&prefix=v" alt="Marketplace"></a>


# SQL Introduction

## IDE Feature flag
At the moment, to work with this course, the following feature flag should be enabled: `edu.course.sql`.

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
- [ ] Test the course by someone available
- [ ] Plugin team -> enable sql course feature flag in prod

### Publication
- [ ] Finalize course name and description
- [ ] Update Readme to match style

## Architecture
### Notes
- Sqlite don't have boolean `TRUE`/`FALSE` -- only `1`/`0` instead

### User-DB interaction
#### JB in-IDE solutions
[Native way](https://www.jetbrains.com/pages/intellij-idea-databases/).
Not available in Community Edition.

#### SQLite CLI
- `sqlite3 prototype.sqlite < init.sql`
- `sqlite3 prototype.sqlite < init_data.sql`
- `sqlite3 prototype.sqlite < src/task.sql`

#### Other GUI app
Problems wit directories, multiplatforms

### Tasks scheme
```text
./
├── init.sql          // (re)create tables
├── init_data.sql     // (re)fill tables with data
├── prototype.sqlite  // DB 
├── src
│   └── task.sql      // User solving task here
└── test
    └── Tests.kt
```

<img src="images/tasks_architecture.png" alt="Anatomy of an edu task in the new checker architecture"/>

### Full Feedback
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
SELECT id, name from Planet where is_inhabited=FALSE;
```