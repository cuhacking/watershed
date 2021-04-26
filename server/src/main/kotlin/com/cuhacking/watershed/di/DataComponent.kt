package com.cuhacking.watershed.di

import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import com.cuhacking.watershed.db.Database
import com.zaxxer.hikari.HikariConfig
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Singleton
@Component
abstract class DataComponent {
    @Singleton
    @Provides
    fun database(dataSource: DataSource): Database = Database(dataSource.asJdbcDriver())

    @Singleton
    @Provides
    open fun dataSource(): DataSource {
        val config = HikariConfig();
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/watershed"
        config.username = "postgres"
        config.password = "password"

        return HikariDataSource(config)
    }
}
