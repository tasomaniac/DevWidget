package com.tasomaniac.devwidget.data.updater

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devwidget.settings.AutoUpdatePreferences
import com.tasomaniac.devwidget.widget.WidgetProvider
import javax.inject.Inject

class ShouldStartWidgetRefreshService @Inject constructor(
    private val app: Application,
    private val autoUpdatePreferences: AutoUpdatePreferences,
    private val appWidgetManager: AppWidgetManager
) {

    fun check(): Boolean {
        return SDK_INT >= O &&
                autoUpdatePreferences.autoUpdate &&
                appWidgetManager.getAppWidgetIds(ComponentName(app, WidgetProvider::class.java)).isNotEmpty()
    }
}
