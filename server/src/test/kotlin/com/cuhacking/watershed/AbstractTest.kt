package com.cuhacking.watershed

import com.cuhacking.watershed.config.Config
import com.cuhacking.watershed.config.ConfigFactory
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import com.cuhacking.watershed.modules.DataModule
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import com.cuhacking.watershed.resources.UserResource
import com.cuhacking.watershed.util.JwtManager
import javax.inject.Inject
import org.flywaydb.core.Flyway;

class PostgresContainer(imageName: String) : org.testcontainers.containers.PostgreSQLContainer<PostgresContainer>(imageName)

class TestMain(config: Config) {

    @Inject
    lateinit var userResource: UserResource

    @Inject
    lateinit var jwtManager: JwtManager

    val config = config

    init {
        val dataModule = DataModule(config)
        val appComponent = DaggerTestComponent.builder()
            .dataModule(dataModule)
            .build()
        appComponent.inject(this)
    }
}

fun Application.configureTest(main: TestMain) {
    installFeatures(main.config, main.jwtManager)
    routing {
        get("/") {
            call.application.environment.log.info("This is a hello world call")
            call.respond("Hello world!")
        }

        with(main.userResource) {
            routing()
        }
    }
}

fun runApplication(main: TestMain): Application.() -> Unit {
    return { configureTest(main) }
}

@Testcontainers
abstract class AbstractTest {

    val DB_USERNAME = "postgres"
    val DB_PASSWORD = "password"

    protected lateinit var testMain: TestMain

    @Container
    val container = PostgresContainer("postgres:latest")
        .withExposedPorts(5432)
        .withDatabaseName("watershed")
        .withUsername(DB_USERNAME)
        .withPassword(DB_PASSWORD)

    @BeforeEach
    fun setup() {
        val address = container.host
        val port = container.firstMappedPort
        val jdbcUrl = "jdbc:postgresql://$address:$port/watershed"
        val config = ConfigFactory.createConfig(jdbcUrl, DB_USERNAME, DB_PASSWORD)
        testMain = TestMain(config)
        val flyway = Flyway.configure()
            .locations("filesystem:build/resources/main/migrations")
            .dataSource(jdbcUrl, DB_USERNAME, DB_PASSWORD)
            .load()
        flyway.migrate()
    }
}