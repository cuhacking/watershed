package config
import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfig(
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/watershed",
    val username: String = "postgres",
    val password: String = ""
)

@Serializable
data class Config(val database: DatabaseConfig)