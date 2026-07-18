import org.junit.Before
import org.junit.Assert
import org.junit.Test


class Test {
    private lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("prototype")
        db.executeFile("init.sql")
        db.executeFile("init_data.sql")
    }

    @Test
    fun testSolution() {

        val results = mutableListOf<EvaluationResult>()
        db.forEachQueryInFile("/task.sql") { db, idx, query ->
            when (idx) {
                0 -> {
                    val expected = listOf(
                        "first_result",
                        "1",)
                    results.add(db.executeAndEvaluate(query, expected, listOf("The moon")))
                }
                1-> {
                    val expected = listOf(
                        "id | name",
                        "3  | Venus",)
                    results.add(db.executeAndEvaluate(query, expected))
                }
                2-> {
                    val expected = listOf(
                        "id | name",
                      //  "2 | Uranus",  // uncomment this line to make the test pass
                        "3 | Venus",
                        "4 | <null>",)
                    results.add(db.executeAndEvaluate(query, expected))
                }
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
