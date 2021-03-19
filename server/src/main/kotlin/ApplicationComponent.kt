import dagger.Component
import javax.inject.Singleton

@Component(modules = [DataModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(main: Main)
}