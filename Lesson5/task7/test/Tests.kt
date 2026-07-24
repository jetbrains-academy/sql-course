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
                    "planet_id | flights",
                    "1 | 4",
                    "5 | 4",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | total | big",
                    "1 | 4 | 2",
                    "2 | 2 | 1",
                    "3 | 2 | 1",
                    "5 | 4 | 2",
                    "42 | 2 | 1",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
