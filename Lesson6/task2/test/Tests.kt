import org.junit.Before
import org.junit.Assert
import org.junit.Test

class Tests {
    lateinit var db: Db

    @Before
    fun setUp() {
        db = Db("test")
        db.executeFile("init.sql")
        db.executeFile("init_data.sql")
    }

    @Test
    fun testSolution() {
        val results = mutableListOf<EvaluationResult>()
        db.forEachQueryInFile("/task.sql") { db, idx, query ->
            when (idx) {
                0 -> results.add(db.executeAndEvaluate(query, listOf(
                    "max_flights",
                    "5",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "busy_groups",
                    "3",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
