package com.tasomaniac.devwidget.widget.click

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ClickBindingsModule {

    @ContributesAndroidInjector(modules = [ClickHandlingModule::class])
    fun clickHandlingActivity(): ClickHandlingActivity

    @ContributesAndroidInjector(modules = [WidgetRefreshModule::class])
    fun widgetRefreshActivity(): WidgetRefreshActivity

    @ContributesAndroidInjector(modules = [HeaderOptionsModule::class])
    fun headerOptionsActivity(): HeaderOptionsActivity
}
