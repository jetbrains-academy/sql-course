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
                    "id | name | is_inhabited | climate",
                    "42 | Answer | 1 | mild",
                )))
                1 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name | is_inhabited | climate",
                    "2 | Aquara | 1 | warm",
                    "3 | Pyros | 0 | hot",
                    "6 | Dunar | 0 | warm",
                    "7 | Solmar | 0 | hot",
                    "12 | Zephyra | 0 | warm",
                )))
                2 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name | is_inhabited | climate",
                    "1 | Terra | 1 | mild",
                    "5 | Verda | 1 | mild",
                    "9 | Cobar | 0 | mild",
                    "42 | Answer | 1 | mild",
                )))
                3 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name | radius",
                    "1 | Terra | 6371",
                    "2 | Aquara | 7000",
                    "5 | Verda | 6800",
                )))
                4 -> results.add(db.executeAndEvaluate(query, listOf(
                    "id | name | is_inhabited | climate | has_weapons",
                    "1 | Terra | 1 | mild | 0",
                    "42 | Answer | 1 | mild | 0",
                )))
            }
        }
        for (result in results) {
            Assert.assertEquals(result.message, result.expected, result.actual)
        }
    }
}
