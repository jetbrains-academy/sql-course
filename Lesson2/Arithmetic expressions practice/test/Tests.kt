import org.junit.Before
import org.junit.Assert
import org.junit.Test

class SqlTest {
    lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("test")
    }

    @Test
    fun testSolution() {
        val results = mutableListOf<EvaluationResult>()
        db.forEachQueryInFile("/task.sql") { db, idx, query ->
            when (idx) {
                0 -> results.add(db.executeAndEvaluate(query, mapOf("result" to "42")))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
            println(result)
        }
    }
}
