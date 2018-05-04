package com.tasomaniac.devwidget

import com.tasomaniac.devwidget.configure.ConfigureActivity
import com.tasomaniac.devwidget.configure.ConfigureModule
import com.tasomaniac.devwidget.configure.WidgetPinnedReceiver
import com.tasomaniac.devwidget.main.MainActivity
import com.tasomaniac.devwidget.main.MainModule
import com.tasomaniac.devwidget.receivers.PackageAddedReceiver
import com.tasomaniac.devwidget.receivers.PackageRemovedReceiver
import com.tasomaniac.devwidget.settings.SettingsActivity
import com.tasomaniac.devwidget.settings.SettingsFragment
import com.tasomaniac.devwidget.settings.SettingsModule
import com.tasomaniac.devwidget.widget.WidgetProvider
import com.tasomaniac.devwidget.widget.WidgetViewsService
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity
import com.tasomaniac.devwidget.widget.click.ClickHandlingActivity
import com.tasomaniac.devwidget.widget.click.ClickHandlingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BindingModule {

    @ContributesAndroidInjector
    fun packageAddedReceiver(): PackageAddedReceiver

    @ContributesAndroidInjector
    fun packageRemovedReceiver(): PackageRemovedReceiver

    @ContributesAndroidInjector
    fun widgetProvider(): WidgetProvider

    @ContributesAndroidInjector
    fun widgetViewsService(): WidgetViewsService

    @ContributesAndroidInjector
    fun activityChooserActivity(): ActivityChooserActivity

    @ContributesAndroidInjector(modules = [ClickHandlingModule::class])
    fun clickHandlingActivity(): ClickHandlingActivity

    @ContributesAndroidInjector(modules = [ConfigureModule::class])
    fun configureActivity(): ConfigureActivity

    @ContributesAndroidInjector
    fun widgetPinnedReceiver(): WidgetPinnedReceiver

    @ContributesAndroidInjector(modules = [MainModule::class])
    fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    fun settingsActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun settingsFragment(): SettingsFragment
}
