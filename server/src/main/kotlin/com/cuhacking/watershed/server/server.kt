package com.cuhacking.watershed.server

import com.cuhacking.watershed.server.resources.UserResource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.cio.*
import io.ktor.server.engine.embeddedServer
import org.slf4j.event.Level
import java.text.DateFormat
import javax.inject.Inject

class Routes @Inject constructor(private val users: UserResource)

class Main {
    @Inject
    lateinit var routes: Routes

    fun run() {
        embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
            install(CallLogging) {
                level = Level.INFO
            }
            install(AutoHeadResponse)
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                    setPrettyPrinting()
                }
            }

            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build().inject(this@Main)
        }.start(wait = true)
    }
}

fun main() {
    Main().run()
}
