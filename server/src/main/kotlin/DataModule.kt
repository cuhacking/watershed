import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import dagger.Module
import dagger.Provides
import com.cuhacking.watershed.db.Database
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
        return HikariDataSource()
    }
}