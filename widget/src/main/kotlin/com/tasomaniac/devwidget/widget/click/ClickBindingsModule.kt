package com.tasomaniac.devwidget.widget.click

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClickBindingsModule {

    @ContributesAndroidInjector(modules = [ClickHandlingModule::class])
    internal abstract fun clickHandlingActivity(): ClickHandlingActivity

    @ContributesAndroidInjector(modules = [WidgetRefreshModule::class])
    internal abstract fun widgetRefreshActivity(): WidgetRefreshActivity

    @ContributesAndroidInjector(modules = [HeaderOptionsModule::class])
    internal abstract fun headerOptionsActivity(): HeaderOptionsActivity
}
