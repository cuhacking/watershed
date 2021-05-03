import com.charleskorn.kaml.Yaml
import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import dagger.Module
import dagger.Provides
import com.cuhacking.watershed.db.Database
import com.zaxxer.hikari.HikariConfig
import javax.inject.Singleton
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource
import config.Config
import java.io.File

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesDatabase(dataSource: DataSource): Database {
        return Database(dataSource.asJdbcDriver())
    }

    @Provides
    @Singleton
    fun providesDataSource(config: Config): DataSource {
        val hikariConfig = HikariConfig();
        hikariConfig.jdbcUrl = config.database.jdbcUrl
        hikariConfig.username = config.database.username
        hikariConfig.password = config.database.password

        return HikariDataSource(hikariConfig)
    }

    @Provides
    @Singleton
    fun providesConfig(): Config {
        //TODO: variable config path -> command line arg?
        val configString = File("config.yml").readText()
        return Yaml.default.decodeFromString(Config.serializer(), configString)

    }
}