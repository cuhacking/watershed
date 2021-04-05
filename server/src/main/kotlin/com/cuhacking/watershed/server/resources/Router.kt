package com.cuhacking.watershed.server.resources

import io.ktor.application.Application
import io.ktor.routing.*

abstract class Router(application: Application) {
    init {
        application.routing {
            setupRoutes()
        }
    }

    abstract fun Routing.setupRoutes()
}
