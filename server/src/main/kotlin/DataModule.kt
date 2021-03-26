import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import dagger.Module
import dagger.Provides
import com.cuhacking.watershed.db.Database
import com.zaxxer.hikari.HikariConfig
import javax.inject.Singleton
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesDatabase(dataSource: DataSource): Database {
        return Database(dataSource.asJdbcDriver())
    }

    @Provides
    @Singleton
    fun providesDataSource(): DataSource {
        val config = HikariConfig();
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/watershed"
        config.username = "postgres"
        config.password = "password"

        return HikariDataSource(config)
    }
}