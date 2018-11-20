package com.tasomaniac.devwidget.configure

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ConfigureBindingsModule {

    @ContributesAndroidInjector(modules = [ConfigureModule::class])
    fun configureActivity(): ConfigureActivity

    @ContributesAndroidInjector
    fun widgetPinnedReceiver(): WidgetPinnedReceiver
}
