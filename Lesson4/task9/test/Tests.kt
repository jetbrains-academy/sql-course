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
                    "Terra | AF201",
                    "Aquara | AF210",
                    "Pyros | AF088",
                    "Glacia | <null>",
                    "Verda | AF147",
                    "Verda | AF149",
                    "Dunar | <null>",
                    "Solmar | <null>",
                    "Mirren | <null>",
                    "Cobar | <null>",
                    "Frost | <null>",
                    "Zephyra | <null>",
                    "Answer | AF305",
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
