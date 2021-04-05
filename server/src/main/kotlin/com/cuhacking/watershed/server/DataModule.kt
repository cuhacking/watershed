package com.cuhacking.watershed.server

import com.cuhacking.watershed.db.Database
import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import javax.sql.DataSource

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
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/watershed"
        config.username = "postgres"
        config.password = "password"

        return HikariDataSource(config)
    }
}
