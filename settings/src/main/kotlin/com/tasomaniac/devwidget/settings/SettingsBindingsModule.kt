package com.tasomaniac.devwidget.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsBindingsModule {

    @ContributesAndroidInjector
    internal abstract fun settingsActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    internal abstract fun settingsFragment(): SettingsFragment
}
