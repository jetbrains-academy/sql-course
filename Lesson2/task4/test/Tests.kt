import com.fasterxml.jackson.databind.ObjectMapper
import com.jetbrains.edu.sql101.createTestServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.time.temporal.ChronoUnit


class Test {
    lateinit var db: Db
    @Before
    fun setUp() {
        db = Db("L2.T4")
    }

    fun _testSolution() {
        //showComposeUi()
        val results = mutableListOf<ScoredSolution>()
//        db.forEachQueryInFile("/Query.sql") { db, idx, query ->
//            when (idx) {
//                0 -> results.add(db.executeAndScore(query, mapOf("result" to "42")))
//                1 -> results.add(db.executeAndScore(query, mapOf("result" to "Hello, SQL")))
//                2 -> {
//                    results.add(db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(1.0)).then {
//                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf((sqrt(122.0)+0.00001).toString()))
//                    }.then {
//                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(1.1))
//                    }.then {
//                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(sqrt(122.0).toString()))
//                    }.then {
//                        db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(5))
//                    }.then {
//                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(-5))
//                    }.then {
//                        ScoredSolution(query, "All tests passed", 1.0)
//                    })
//                }
//                3 -> {
//                    results.add(
//                            db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("The moon")).then {
//                                db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("the moon"))
//                            }.then {
//                                db.executeAndScore(query, mapOf("result" to "TRUE"), listOf("THE MOON"))
//                            }.then {
//                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf("The  moon"))
//                            }.then {
//                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf("foobar"))
//                            }.then {
//                                db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(""))
//                            }.then {
//                                ScoredSolution(query, "All tests passed", 1.0)
//                            }
//                    )
//                }
//                4 -> {
//                    var result = db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(1)).then {
//                        db.executeAndScore(query, mapOf("result" to "FALSE"), listOf(11))
//                    }
//                    for (k in 2..10) {
//                        result = result.then { db.executeAndScore(query, mapOf("result" to "TRUE"), listOf(k*k)) }
//                    }
//                    results.add(result.then { ScoredSolution(query, "All tests passed", 1.0)})
//                }
//                else -> "OK"
//            }
//        }
//        if (results.any { it.score == 0.0 }) {
//            printAssessments(results)
//        }
    }

    @Test
    fun testStartTestServer() {
        println("Starting server")
        val server = createTestServer().start(wait = false)
        val client  = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()
        try {
            repeat(10) {
                Thread.sleep(1000)
                println("Checking the grading status")
                val request = HttpRequest.newBuilder()
                    .uri(URI("""http://localhost:8080/grade/status?solutionFile=${URLEncoder.encode("/Query.sql")}"""))
                    .timeout(Duration.of(1, ChronoUnit.SECONDS))
                    .GET()
                    .build()
                val response = client.send(request, BodyHandlers.ofString())
                if (response.statusCode() == 200) {
                    val responseJson = ObjectMapper().readTree(response.body())
                    println("received status: $responseJson")
                    val isCompleted = responseJson.get("isCompleted")
                    if (isCompleted.isBoolean && isCompleted.booleanValue()) {
                        val isAllOk = responseJson.get("isAllOk")
                        assertNotNull("Some checks have failed", isAllOk)
                        if (isAllOk.isBoolean) {
                            assertTrue("Some checks have failed", isAllOk.booleanValue())
                            return
                        }
                    } else {
                        println("grading is not yet completed")
                    }
                } else {
                    println("response status=${response.statusCode()}")
                }
            }
            fail("Checks timed out")
        } finally {
            println("Stopping server")
            server.stop()
        }
    }
}