package com.jetbrains.edu.sql101

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.jetbrains.edu.sql101.plugins.configureRouting
import com.jetbrains.edu.sql101.plugins.configureSerialization
import io.ktor.server.resources.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
fun main() {
    createTestServer().also {
        println(Json { prettyPrint = true }.encodeToString(SolutionTestParameters(solutionFile = "/foo.sql", queryTestParameters = listOf(
            QueryTestParameters(queryNum = 1, expectedResult = "42", placeholderValues = mapOf(
                "foo" to QueryStringParameter("foo"), "bar" to QueryIntParameter(42)
            ))
        ))))
    }.start(wait = true)
}

fun createTestServer() = embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)


fun Application.module() {
    configureSerialization()
    configureRouting()
    install(Resources)
}
