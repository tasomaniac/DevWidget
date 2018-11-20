package com.tasomaniac.devwidget.widget

import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface WidgetBindingsModule {

    @ContributesAndroidInjector
    fun widgetProvider(): WidgetProvider

    @ContributesAndroidInjector
    fun widgetViewsService(): WidgetViewsService

    @ContributesAndroidInjector
    fun activityChooserActivity(): ActivityChooserActivity
}
