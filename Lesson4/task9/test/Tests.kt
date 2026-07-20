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
                    "planet | num",
                    "Terra | MF201",
                    "Aquara | MF210",
                    "Pyros | MF088",
                    "Glacia | <null>",
                    "Verda | MF147",
                    "Verda | MF149",
                    "Dunar | <null>",
                    "Solmar | <null>",
                    "Mirren | <null>",
                    "Cobar | <null>",
                    "Frost | <null>",
                    "Zephyra | <null>",
                    "Answer | MF305",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "planet",
                    "Glacia",
                    "Dunar",
                    "Solmar",
                    "Mirren",
                    "Cobar",
                    "Frost",
                    "Zephyra",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
