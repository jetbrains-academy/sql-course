import org.junit.Assert
import org.junit.Test

class Test {
    fun task6_solution(inputData: List<String>): Map<String, Int?> {
        val mapRegionToSum = mutableMapOf<String, Int?>()
        for (row in inputData) {
            val columns = row.split(",")
            if ("Asia" != columns[5]) {
                mapRegionToSum[columns[5]] = mapRegionToSum.getOrDefault(columns[5], 0)!! + columns[6].toInt()
            }
        }
        return mapRegionToSum
    }
    @Test fun testSolution() {
        val data = Test::class.java.getResourceAsStream("/airports.csv")?.reader()?.readLines()?.drop(1) ?: throw RuntimeException("Can't read the input file")
        Assert.assertEquals(task6_solution(data), task6_totalByRegionExceptAsia(data))
    }
}