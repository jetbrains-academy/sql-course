import org.junit.Before
import org.junit.Assert
import org.junit.Test
import kotlin.math.sqrt

class Tests {
    lateinit var db: Db

    @Before
    fun setUp() {
        db = Db("test", true)
    }

    @Test
    fun testSolution() {
        val results = mutableListOf<EvaluationResult>()
        db.forEachQueryInFile("/task.sql") { db, idx, query ->
            when (idx) {
                0 -> {
                    results.add(db.executeAndEvaluate(query, listOf("result", "0"), listOf(1.0)))
                    results.add(db.executeAndEvaluate(query, listOf("result", "0"), listOf((sqrt(122.0) + 0.00001))))
                    results.add(db.executeAndEvaluate(query, listOf("result", "1"), listOf(1.1)))
                    results.add(db.executeAndEvaluate(query, listOf("result", "1"), listOf(sqrt(122.0))))
                    results.add(db.executeAndEvaluate(query, listOf("result", "1"), listOf(5)))
                    results.add(db.executeAndEvaluate(query, listOf("result", "0"), listOf(-5)))
                }
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
