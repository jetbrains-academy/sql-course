import org.junit.Before
import org.junit.Test
import kotlin.math.sqrt
import com.jetbrains.edu.sql101.createTestServer
import org.junit.Assert
import org.junit.Assert.assertTrue
import java.net.URL

class Test {
    lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("L2.T4")
    }

    fun _testSolution() {
        //showComposeUi()
        val results = mutableListOf<ScoredSolution>()
        db.forEachQueryInFile("/Query.sql") { db, idx, query ->
            when (idx) {
                0 -> results.add(db.executeAndScore(query, mapOf("result" to "42")))
                1 -> results.add(db.executeAndScore(query, mapOf("result" to "Hello, SQL")))
                2 -> {
                    results.add(db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(1.0)).then {
                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf((sqrt(122.0)+0.00001).toString()))
                    }.then {
                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(1.1))
                    }.then {
                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(sqrt(122.0).toString()))
                    }.then {
                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(5))
                    }.then {
                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(-5))
                    }.then {
                        ScoredSolution(query, "All tests passed", 1.0)
                    })
                }
                3 -> {
                    results.add(
                            db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("The moon")).then {
                                db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("the moon"))
                            }.then {
                                db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("THE MOON"))
                            }.then {
                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf("The  moon"))
                            }.then {
                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf("foobar"))
                            }.then {
                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(""))
                            }.then {
                                ScoredSolution(query, "All tests passed", 1.0)
                            }
                    )
                }
                4 -> {
                    var result = db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(1)).then {
                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(11))
                    }
                    for (k in 2..10) {
                        result = result.then { db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(k*k)) }
                    }
                    results.add(result.then { ScoredSolution(query, "All tests passed", 1.0)})
                }
                else -> "OK"
            }
        }
        if (results.any { it.score == 0.0 }) {
            printAssessments(results)
        }
    }

    @Test
    fun testStartTestServer() {

        val server = createTestServer().start(wait = false)
        println()
        Assert.fail(URL("http://localhost:8080").openStream().reader().readText())
        server.stop()
        println("--------")
    }
}