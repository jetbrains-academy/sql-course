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
                    "id | name",
                    "6 | Dunar",
                    "9 | Cobar",
                    "12 | Zephyra",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name",
                    "1 | Terra",
                    "2 | Aquara",
                    "5 | Verda",
                    "42 | Answer",
                )))
                2 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name",
                    "2 | Aquara",
                    "5 | Verda",
                    "7 | Solmar",
                    "8 | Mirren",
                    "9 | Cobar",
                    "12 | Zephyra",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
