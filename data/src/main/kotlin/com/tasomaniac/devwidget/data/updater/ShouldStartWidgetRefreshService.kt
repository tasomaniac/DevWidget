package com.tasomaniac.devwidget.data.updater

import com.tasomaniac.devwidget.settings.AutoUpdatePreferences
import com.tasomaniac.devwidget.widget.WidgetUpdater
import javax.inject.Inject

class ShouldStartWidgetRefreshService @Inject constructor(
    private val autoUpdatePreferences: AutoUpdatePreferences,
    private val widgetUpdater: WidgetUpdater
) {

    fun check(): Boolean {
        return autoUpdatePreferences.autoUpdate && widgetUpdater.hasWidgets()
    }
}
