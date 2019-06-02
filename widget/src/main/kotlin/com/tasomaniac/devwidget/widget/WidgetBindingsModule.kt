package com.tasomaniac.devwidget.widget

import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WidgetBindingsModule {

    @Binds
    abstract fun widgetUpdater(widgetUpdater: WidgetUpdaterImpl): WidgetUpdater

    @ContributesAndroidInjector
    internal abstract fun widgetProvider(): WidgetProvider

    @ContributesAndroidInjector
    internal abstract fun widgetViewsService(): WidgetViewsService

    @ContributesAndroidInjector
    internal abstract fun activityChooserActivity(): ActivityChooserActivity
}
