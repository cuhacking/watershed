package com.cuhacking.watershed.di

import me.tatarka.inject.annotations.Component
import resources.UserResource

@Singleton
@Component
abstract class ApplicationComponent(@Component val dataComponent: DataComponent) {
    abstract val userResource: UserResource
}
