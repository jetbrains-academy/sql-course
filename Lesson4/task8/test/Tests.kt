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
                    "Verda | MF147",
                    "Verda | MF149",
                    "Aquara | MF210",
                    "Answer | MF305",
                    "Pyros | MF088",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "flight_date",
                    "2122-04-12",
                    "2122-05-01",
                    "2122-05-08",
                    "2122-05-12",
                    "2122-06-01",
                )))
                2 -> results.add(db.executeAndEvaluate(query, listOf(
                    "planet | num | ship",
                    "Terra | MF201 | Falcon 22",
                    "Verda | MF147 | Falcon 28",
                    "Verda | MF149 | Falcon 25",
                    "Aquara | MF210 | Falcon 22",
                    "Answer | MF305 | Falcon 28",
                    "Pyros | MF088 | Falcon 25",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
