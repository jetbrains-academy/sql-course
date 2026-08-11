As data volume and search complexity grow, spreadsheets and 
text-based command-line tools start to reach their limits. What if you need to calculate the total number of 
movements across all airports for each country? Or calculate the average number of 
movements for all airports outside North America? 

Spreadsheets can filter data, as well as group and aggregate values using pivot tables:

<div style="text-align: center; max-width: 800px; margin: 0 auto;">
<img src="images/google_sheets.png" alt="Google sheets example">
</div>

A good knowledge of Unix command-line text-processing tools may also help. For instance, here is how you can calculate 
the total number of movements by region using the **`awk`** tool:

```
cd Lesson1
cat airports.csv | awk -F, 'NR>1 {arr[$6]+=$7} END {for (a in arr) print a, arr[a]}'
```

<div class="hint" title="Result">

```
North America 9403080
Asia 3101847
Europe 2348417
```
</div>


However, as datasets grow larger, queries become more intricate, and we require faster, more frequent results. 
At this point, manual or semi-automated spreadsheet tweaks and cumbersome shell scripts are no longer enough.

You may argue that we could write custom search logic using a general-purpose programming language like Kotlin or Python.
This works well when all of your data fits into RAM and your queries are known in advance, allowing you to write 
efficient code with reasonable effort. In the next task, you can try writing simple search logic in Kotlin.
