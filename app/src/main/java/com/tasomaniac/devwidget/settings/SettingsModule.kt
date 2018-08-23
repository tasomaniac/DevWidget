package com.tasomaniac.devwidget.settings

import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet

@Module
class SettingsModule {

    @Provides
    @ElementsIntoSet
    fun settings(
        general: GeneralSettings,
        display: DisplaySettings,
        advancedSettings: AdvancedSettings,
        other: OtherSettings
    ) = setOf(general, display, advancedSettings, other)
}
