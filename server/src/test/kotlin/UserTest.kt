import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.GenericContainer as JGenericContainer
import kotlin.test.*

class GenericContainer(imageName: String) : JGenericContainer<GenericContainer>(imageName)

fun Application.configureTest() {
    installFeatures()
    // Other stuff
}

@Testcontainers
class UserTest {

    @Container
    val container = GenericContainer("postgres:latest").withExposedPorts(5432)

//    @Test
//    fun testCreateUser() = withTestApplication(Application::configureTest) {
//        with(handleRequest(HttpMethod.Get, "/user") {
//        }) {
//
//        }
//    }

}