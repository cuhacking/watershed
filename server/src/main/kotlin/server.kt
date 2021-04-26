import com.cuhacking.watershed.di.ApplicationComponent
import com.cuhacking.watershed.di.DataComponent
import com.cuhacking.watershed.di.create
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.*
import io.ktor.server.engine.embeddedServer
import org.slf4j.event.Level

fun main() {
    val dataComponent: DataComponent = DataComponent::class.create()
    val appComponent: ApplicationComponent = ApplicationComponent::class.create(dataComponent)

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

            with(appComponent.userResource) {
                routing()
            }
        }
    }.start(wait = true)
}