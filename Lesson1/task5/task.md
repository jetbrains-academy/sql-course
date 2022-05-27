When either the amount of data, or the search task complexity, or both of them grow more, spreadsheets or 
text-based command line tools become less usable. What if you want to calculate for each country the total count of 
movements summed across all airports from that country? What if you want to calculate the average count of 
movements, excluding airports from North America? 

Spreadsheets can actually filter data, group and calculate aggregated values for groups using Pivot tables:

[Demo of the using pivot table in Google Sheets goes here] 


Good knowledge of Unix command-line text processing tools may help as well. For instance, here is how we can calculate 
the total sum by region using `awk` tool:

```
cd datasets/small
cat airports.csv | awk -F, 'NR>1 {arr[$6]+=$7} END {for (a in arr) print a, arr[a]}'
```

However, data grows bigger, search requests become more complex, and we need to search more often and want the results fast. 
We need something different from manual or semi-automated manipulations with the spreadsheets or writing awkward shell scripts.
One may argue that we can write the search code using a general-purpose programming language, such as Kotlin or Python.
This may work well if all our data fits into RAM and all our queries are known in advance, which allows for writing 
efficient code with relatively small efforts. In the next task you can try to write simple search code in Kotlin.