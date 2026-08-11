Look at the [airports.csv](course://Lesson1/airports.csv) file in this lesson folder.

<div style="text-align: center; max-width: 300px; margin: 0 auto;">
<img src="images/airports_file.png" alt="airports.csv file location">
</div>

As your dataset grows, searching manually becomes unreliable. It's easy to miss a row, double-count a value,
or make other small errors.

To search reliably in tables with more than a few dozen rows, you need automation. Spreadsheets, like Microsoft Excel or Google Sheets, are handy for quick and relatively simple tasks, such as finding 
the maximum or minimum values, because they let you easily filter or sort rows by any column. 

If you are a command-line addict, you can use command-line tools for such tasks. For instance, 
the simple **`sort`** utility can order input by any specified column to reveal maximum or minimum values.
If you're using Linux, macOS, or even Windows with Linux tools enabled, open the %IDE_NAME% Terminal (*&shortcut:ActivateTerminalToolWindow;*) and try running
these commands to find the busiest airport in a larger dataset:  

```
cd Lesson1
cat airports.csv | sort -t, -k7 -n -r | head -n 1
```
