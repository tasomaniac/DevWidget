package com.tasomaniac.devwidget

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devwidget.data.updater.ShouldStartWidgetRefreshService
import com.tasomaniac.devwidget.data.updater.WidgetRefreshService
import com.tasomaniac.devwidget.settings.NightModePreferences
import dagger.android.support.DaggerApplication
import javax.inject.Inject

abstract class BaseDevWidgetApp : DaggerApplication() {

    @Inject lateinit var nightModePreferences: NightModePreferences
    @Inject lateinit var shouldStartWidgetRefreshService: ShouldStartWidgetRefreshService

    override fun onCreate() {
        super.onCreate()
        nightModePreferences.updateDefaultNightMode()

        if (SDK_INT >= O && shouldStartWidgetRefreshService.check()) {
            startForegroundService(Intent(this, WidgetRefreshService::class.java))
        }
    }
}
