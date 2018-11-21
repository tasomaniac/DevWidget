package com.tasomaniac.devwidget.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface SettingsBindingsModule {

    @ContributesAndroidInjector
    fun settingsActivity(): SettingsActivity

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    fun settingsFragment(): SettingsFragment
}
