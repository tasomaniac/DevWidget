package com.tasomaniac.devwidget.app

import com.tasomaniac.devwidget.configure.ConfigureBindingsModule
import com.tasomaniac.devwidget.data.updater.DataUpdaterBindingModule
import com.tasomaniac.devwidget.main.MainActivity
import com.tasomaniac.devwidget.main.MainModule
import com.tasomaniac.devwidget.settings.SettingsBindingsModule
import com.tasomaniac.devwidget.widget.WidgetBindingsModule
import com.tasomaniac.devwidget.widget.click.ClickBindingsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(
    includes = [
        ConfigureBindingsModule::class,
        ClickBindingsModule::class,
        DataUpdaterBindingModule::class,
        SettingsBindingsModule::class,
        WidgetBindingsModule::class
    ]
)
interface BindingModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    fun mainActivity(): MainActivity
}
