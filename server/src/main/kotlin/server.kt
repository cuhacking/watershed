import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.*
import io.ktor.server.engine.embeddedServer
import resources.UserResource
import org.slf4j.event.Level
import javax.inject.Inject

class Main {
    private val appComponent = DaggerApplicationComponent.create()

    @Inject
    lateinit var userResource: UserResource

    init {
        appComponent.inject(this)
    }
}

fun main() {

    val main = Main()
    embeddedServer(CIO, port = 8080, host = "127.0.0.1") {
        install(CallLogging) {
            level = Level.DEBUG
        }
        install(AutoHeadResponse)

        routing {
            get("/") {
                call.application.environment.log.info("This is a hello world call")
                call.respond("Hello world!")
            }

            with(main.userResource) {
                routing()
            }
        }
    }.start(wait = true)
}