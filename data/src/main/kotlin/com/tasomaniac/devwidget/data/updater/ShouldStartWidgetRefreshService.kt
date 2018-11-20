package com.tasomaniac.devwidget.data.updater

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devwidget.settings.AutoUpdatePreferences
import com.tasomaniac.devwidget.widget.WidgetUpdater
import javax.inject.Inject

class ShouldStartWidgetRefreshService @Inject constructor(
    private val autoUpdatePreferences: AutoUpdatePreferences,
    private val widgetUpdater: WidgetUpdater
) {

    fun check(): Boolean {
        return SDK_INT >= O &&
                autoUpdatePreferences.autoUpdate &&
                widgetUpdater.hasWidgets()
    }
}
