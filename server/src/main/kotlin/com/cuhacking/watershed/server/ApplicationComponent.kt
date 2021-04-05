package com.cuhacking.watershed.server

import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, DataModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(main: Main)
}
