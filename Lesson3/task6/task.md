From this lesson on, every lesson ships a small SQLite database (this one has `L3_planet.sqlite`).
You can open it right inside %IDE_NAME% with the built-in [**Database** tools](https://www.jetbrains.com/help/idea/relational-databases.html) to browse the data and run
the example queries — no external program needed.

> **Prerequisite.** The Database tools are bundled in **IntelliJ IDEA Ultimate**. 
> In **IntelliJ IDEA Community** they are not available. If you don't have them,
> you can use the `sqlite3` [command-line client](https://www.sqlite.org/cli.html) instead.


To work with the database, first create a **data source** — a saved connection to the database file:

1. Double-click the database file in the lesson structure (`L3_planet.sqlite` for the current lesson). 
   You will see the **Data Sources and Drivers** window:
<div style="text-align: center; max-width: 800px; margin: 0 auto;">
<img src="images/data_sources.png" alt="Data Sources and Drivers window">
</div>

2. If you see a note that the driver is not downloaded, click the **Download** button and wait until it finishes:
<div style="text-align: center; max-width: 600px; margin: 0 auto;">
<img src="images/driver_download.png" alt="Driver not downloaded message">
</div>

3. Click **Test Connection** at the bottom to check that everything is working:
<div style="text-align: center; max-width: 400px; margin: 0 auto;">
<img src="images/data_sources_connection_ok.png" alt="Test Connection results">
</div>

4. Finally, click **OK** to finish the setup.

5. The data source now appears in the **Database** tool window on the right. In this lesson the database contains only one table, `Planet`.
   You can find it by expanding **L3_planet** → **main** → **tables**.

<div style="text-align: center; max-width: 1000px; margin: 0 auto;">
<img src="images/planet_table.png" alt="Planet table content">
</div>

Each lesson has its own database file
(`L4_astrofleet.sqlite`, `L5_astrofleet.sqlite`, …) — connect to the current lesson's file the same way at the beginning of the lesson.

If you need more details about the Database tool, feel free to read the [official help page](https://www.jetbrains.com/help/idea/sqlite.html).

