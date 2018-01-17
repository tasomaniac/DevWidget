package com.tasomaniac.devwidget

import com.tasomaniac.devwidget.data.DataModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BindingModule::class,
        AnalyticsModule::class,
        DataModule::class
    ]
)
interface AppComponent : AndroidInjector<DevWidgetApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DevWidgetApp>()
}
