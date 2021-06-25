package com.cuhacking.watershed

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.cuhacking.watershed.config.Config
import com.cuhacking.watershed.config.ConfigFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.embeddedServer
import com.cuhacking.watershed.modules.DataModule
import com.cuhacking.watershed.resources.AuthResource
import com.cuhacking.watershed.resources.UserResource
import com.cuhacking.watershed.util.JwtManager
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import org.slf4j.event.Level
import javax.inject.Inject

class Arguments(parser: ArgParser) {
    val config by parser.storing("--config", help = "Path to config file, relative to root of the project")
        .default("config.yml")
}

class Main(configPath: String) {

    @Inject
    lateinit var config: Config

    @Inject
    lateinit var jwtManager: JwtManager

    // Resources
    @Inject
    lateinit var userResource: UserResource

    @Inject
    lateinit var authResource: AuthResource

    init {
        val config = ConfigFactory.createConfig(configPath)
        val dataModule = DataModule(config)
        val appComponent = DaggerApplicationComponent.builder()
            .dataModule(dataModule)
            .build()
        appComponent.inject(this)
    }
}

fun Application.installFeatures(config: Config, jwtManager: JwtManager) {
    install(CallLogging) {
        level = Level.DEBUG
    }
    install(AutoHeadResponse)
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = config.auth.issuer
            verifier(JWT
                .require(jwtManager.algorithm)
                .withIssuer(config.auth.issuer)
                .build())
            validate { credential ->
                jwtManager.validate(credential)
            }
        }
    }
}

private fun Application.configure(args: Array<String>) {
    ArgParser(args).parseInto(::Arguments).run {
        val main = Main(config)
        installFeatures(main.config, main.jwtManager)
        routing {
            get("/") {
                call.application.environment.log.info("This is a hello world call")
                call.respond("Hello world!")
            }

            with(main.userResource) {
                routing()
            }

            with(main.authResource) {
                routing()
            }
        }
    }
}

fun main(args: Array<String>) {
    val server = embeddedServer(CIO, port = 8080, host = "127.0.0.1") {
        configure(args)
    }
    server.start(wait = true)
}