package com.cuhacking.watershed

import dagger.Component
import com.cuhacking.watershed.modules.DataModule
import javax.inject.Singleton

@Component(modules = [DataModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(main: Main)
}