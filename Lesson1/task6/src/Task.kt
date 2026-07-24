/**
 * This function takes the input CSV data and returns a mapping of the region name to the total sum
 * of movements in the airports in that region. The mapping includes all the regions which can be found
 * in the input data, except for Asia.
 *
 * The input data are the rows of CSV file with the data cells separated by commas.
 */
fun task6_totalByRegionExceptAsia(inputData: List<String>): Map<String, Int> {
    val mapRegionToSum = mutableMapOf<String, Int>()
    for (row in inputData) {
        val columns = row.split(",")
        if ("Asia" != columns[5]) {
            mapRegionToSum[columns[5]] = mapRegionToSum.getOrDefault(columns[5], 0) + columns[6].toInt()
        }
    }
    return mapRegionToSum
}