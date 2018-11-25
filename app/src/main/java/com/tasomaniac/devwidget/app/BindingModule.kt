package com.tasomaniac.devwidget.app

import com.tasomaniac.devwidget.configure.ConfigureBindingsModule
import com.tasomaniac.devwidget.data.updater.DataUpdaterBindingModule
import com.tasomaniac.devwidget.main.MainBindingsModule
import com.tasomaniac.devwidget.settings.SettingsBindingsModule
import com.tasomaniac.devwidget.widget.WidgetBindingsModule
import com.tasomaniac.devwidget.widget.click.ClickBindingsModule
import dagger.Module

@Module(
    includes = [
        MainBindingsModule::class,
        ConfigureBindingsModule::class,
        ClickBindingsModule::class,
        DataUpdaterBindingModule::class,
        SettingsBindingsModule::class,
        WidgetBindingsModule::class
    ]
)
interface BindingModule
