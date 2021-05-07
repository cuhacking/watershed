package com.cuhacking.watershed.config
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class DatabaseConfig(
    val jdbcUrl: String = "jdbc:postgresql://localhost:5432/watershed",
    val username: String = "postgres",
    val password: String = ""
)

@Serializable
data class Config(val database: DatabaseConfig)

class ConfigFactory {
    companion object {
        fun createConfig(jdbcUrl: String, username: String, password: String) : Config {
            val databaseConfig = DatabaseConfig(jdbcUrl, username, password)
            return Config(databaseConfig)
        }

        fun createConfig(filePath: String) : Config {
            val configString = File(filePath).readText()
            return Yaml.default.decodeFromString(Config.serializer(), configString)
        }
    }

}