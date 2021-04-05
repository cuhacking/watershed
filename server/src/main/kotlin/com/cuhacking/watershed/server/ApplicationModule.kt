package com.cuhacking.watershed.server

import dagger.Module
import dagger.Provides
import io.ktor.application.*

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun providesApplication(): Application = application
}
