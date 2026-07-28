From this lesson on, every lesson ships a small SQLite database (this one has `L3_planet.sqlite`).
You can open it right inside %IDE_NAME% with the built-in **Database** tools to browse the data and run
the example queries — no external program needed.

> **Prerequisite.** The Database tools are bundled in **IntelliJ IDEA Ultimate** (and in **DataGrip** and
> **PyCharm Professional**). In **IntelliJ IDEA Community** they are not available. If you don't have them,
> you can use the `sqlite3` command-line client instead (see Lesson 1): run `sqlite3 L3_planet.sqlite`.

To work with the database, first create a **data source** — a saved connection to the database file:

1. Open the **Database** tool window: `View | Tool Windows | Database`.
2. On its toolbar, click **New** (**+**) → **Data Source** → **SQLite** (or use the main menu
   `File | New | Data Source | SQLite`).
3. In the **Data Sources and Drivers** dialog, keep the default **Xerial SQLite JDBC** driver. If a
   **Download missing driver files** link appears at the bottom, click it once.
4. Leave **Connection type** as **default** and set the **File** field to this lesson's database
   `L3_planet.sqlite`: click the **Open** (…) button and pick the file.
   *Shortcut:* you can instead **drag `L3_planet.sqlite` from the Project view straight into the Database
   tool window**, and the data source is created for you.
5. Click **Test Connection** to make sure it works, then click **OK**.

The data source now appears in the **Database** tool window. Each lesson has its own database file
(`L4_astrofleet.sqlite`, `L5_astrofleet.sqlite`, …) — connect to the current lesson's file the same way.
