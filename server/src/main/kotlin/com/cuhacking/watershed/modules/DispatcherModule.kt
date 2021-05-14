package com.cuhacking.watershed.modules

import com.cuhacking.watershed.common.CoroutineDispatchers
import com.cuhacking.watershed.db.Database
import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton
import javax.sql.DataSource

@Module
class DispatcherModule {

    @Provides
    @Singleton
    fun providesDispatcher(): CoroutineDispatcher {
        return CoroutineDispatchers.io
    }

}