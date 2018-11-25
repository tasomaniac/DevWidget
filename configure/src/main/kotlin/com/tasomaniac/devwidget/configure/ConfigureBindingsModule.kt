package com.tasomaniac.devwidget.configure

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ConfigureBindingsModule {

    @ContributesAndroidInjector(modules = [ConfigureModule::class])
    internal abstract fun configureActivity(): ConfigureActivity

    @ContributesAndroidInjector
    internal abstract fun widgetPinnedReceiver(): WidgetPinnedReceiver
}
