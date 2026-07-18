import org.junit.Before
import org.junit.Assert
import org.junit.Test

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
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "1"), listOf("The moon")))
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "1"), listOf("the moon")))
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "1"), listOf("THE MOON")))
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "0"), listOf("The  moon")))
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "0"), listOf("foobar")))
                    results.add(db.executeAndEvaluate(query, listOf("first_result", "0"), listOf("")))
                }
                1 -> {
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "1"), listOf(4)))
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "1"), listOf(9)))
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "1"), listOf(100)))
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "0"), listOf(1)))
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "0"), listOf(5)))
                    results.add(db.executeAndEvaluate(query, listOf("second_result", "0"), listOf(121)))
                }
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
