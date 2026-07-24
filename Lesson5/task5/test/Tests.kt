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
                    "climate | max_radius",
                    "cold | 5200",
                    "extremely hot | 8200",
                    "hot | 9800",
                    "mild | 7100",
                    "warm | 12000",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | flights",
                    "1 | 4",
                    "2 | 3",
                    "3 | 3",
                    "4 | 4",
                    "5 | 0",
                )))
                2 -> results.add(db.executeAndEvaluate(query, listOf(
                    "planet_id | spacecraft_id | flights",
                    "1 | 1 | 2",
                    "1 | 4 | 2",
                    "2 | 1 | 1",
                    "2 | 4 | 1",
                    "3 | 2 | 1",
                    "3 | 3 | 1",
                    "5 | 1 | 1",
                    "5 | 2 | 1",
                    "5 | 3 | 1",
                    "5 | 4 | 1",
                    "42 | 2 | 1",
                    "42 | 3 | 1",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
