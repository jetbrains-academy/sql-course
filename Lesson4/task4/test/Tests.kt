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
                    "Verda | AF147",
                    "Verda | AF149",
                    "Aquara | AF210",
                    "Answer | AF305",
                    "Pyros | AF088",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "flight_date",
                    "2122-04-12",
                    "2122-05-01",
                    "2122-05-08",
                    "2122-05-12",
                    "2122-06-01",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
