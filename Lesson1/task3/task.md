When the amount of data grows, search with eyes becomes troublesome. You can easily miss some value, or count twice,
or do other mistakes.
If you want to search reliably within a table with more than a few dozens of rows,  
you need some automation. Spreadsheets, like Excel or Google Sheets, are handy for many quick and relatively simple tasks, like finding 
maximum or minimum values, because they allow for filtering the rows or sorting the rows by any column. 

If you are a command-line addict, you can use command-line tools for such tasks. For instance, 
the simple `sort` utility can sort its input by any column and thus find a maximum or minimum value in that column
and the row where that value is found. If you're on Linux, macOS or even Windows with Linux tools, try running
these commands in %IDE_NAME% Terminal (hit &shortcut:ActivateTerminalToolWindow;) to find the busiest airport in a bigger dataset:  

```
cd datasets/small
cat airports.csv | sort -t, -k7 -n -r | head -n 1
```

