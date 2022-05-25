When the amount of data grows, search with eyes becomes troublesome. You can easily miss some value, or count twice,
or do some other mistake.
If you want to search reliably within a table with more than a few dozens of rows, 
you need some automated tools.
Spreadsheets, like Excel or Google Sheets, are handy for many quick and relatively simple tasks, like finding 
maximum or minimum values, because they allow for sorting the rows by any column. 

If you are a command-line addict, you can use command-line tools for such tasks. For instance, 
the simple `sort` utility can sort its input by any column and thus find a maximum or minimum value in that column
and the row where that value is found. If you're on Linux, macOS or even Windows with Linux tools, try running
these commands in %IDE_NAME% Terminal (hit &shortcut:ActivateTerminalToolWindow;) to find the country with the highest death rate:  

```
cd datasets
cat traffic_death_rates.csv | sort -t, -k3,4 -n -r | head -n 1
```

