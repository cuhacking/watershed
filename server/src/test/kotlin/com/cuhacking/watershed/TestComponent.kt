package com.cuhacking.watershed

import dagger.Component
import com.cuhacking.watershed.modules.DataModule
import com.cuhacking.watershed.modules.DispatcherModule
import javax.inject.Singleton

@Component(modules = [DataModule::class, DispatcherModule::class])
@Singleton
interface TestComponent : ApplicationComponent {
    fun inject(main: TestMain)
}