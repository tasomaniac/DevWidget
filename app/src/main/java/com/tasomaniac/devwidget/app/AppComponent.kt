package com.tasomaniac.devwidget.app

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
        AnalyticsModule::class,
        BindingModule::class,
        AnalyticsModule::class,
        DataModule::class
    ]
)
interface AppComponent : AndroidInjector<DevWidgetApp> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<DevWidgetApp>
}
