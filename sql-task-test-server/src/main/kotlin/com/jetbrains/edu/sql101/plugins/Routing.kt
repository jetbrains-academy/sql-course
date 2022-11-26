package com.jetbrains.edu.sql101.plugins

import com.jetbrains.edu.sql101.SolutionTestParameters
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/assess") {
            val params = call.receive<SolutionTestParameters>()
            println(params)
            call.respondText("Ok")
        }
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
