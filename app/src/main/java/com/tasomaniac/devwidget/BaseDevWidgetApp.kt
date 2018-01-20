package com.tasomaniac.devwidget

import com.tasomaniac.devwidget.settings.NightModePreferences
import dagger.android.support.DaggerApplication
import javax.inject.Inject

abstract class BaseDevWidgetApp : DaggerApplication() {

    @Inject lateinit var nightModePreferences: NightModePreferences

    override fun onCreate() {
        super.onCreate()
        nightModePreferences.updateDefaultNightMode()
    }
}
