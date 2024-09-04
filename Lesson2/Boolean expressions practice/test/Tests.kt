import org.junit.Before
import org.junit.Assert
import org.junit.Test
import kotlin.math.sqrt

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
                0 -> {
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "FALSE"), listOf(1.0)))
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "FALSE"), listOf((sqrt(122.0)+0.00001).toString())))
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "TRUE" ), listOf(1.1)))
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "TRUE" ), listOf(sqrt(122.0).toString())))
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "TRUE" ), listOf(5)))
                    results.add(db.executeAndEvaluate(query, mapOf("result" to "FALSE"), listOf(-5)))
                }
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
            println(result)
        }
    }
}
