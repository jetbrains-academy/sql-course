import org.junit.Before
import org.junit.Assert
import org.junit.Test


class Test {
    lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("test")
    }

    @Test
    fun testSolution() {
        //showComposeUi()
        val results = mutableListOf<EvaluationResult>()
        db.forEachQueryInFile("/task.sql") { db, idx, query ->
            when (idx) {

                0 -> {
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "TRUE"), listOf("The moon")))
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "TRUE"), listOf("the moon")))
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "TRUE"), listOf("THE MOON")))
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "FALSE"), listOf("The  moon")))
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "FALSE"), listOf("foobar")))
                    results.add(db.executeAndEvaluate(query, mapOf("first_result" to "FALSE"), listOf("")))
                }
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
