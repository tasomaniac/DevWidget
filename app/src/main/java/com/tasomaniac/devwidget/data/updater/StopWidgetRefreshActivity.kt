package com.tasomaniac.devwidget.data.updater

import android.content.Intent
import android.os.Bundle
import com.tasomaniac.devwidget.settings.AutoUpdatePreferences
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class StopWidgetRefreshActivity : DaggerAppCompatActivity() {

    @Inject lateinit var autoUpdatePreferences: AutoUpdatePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        autoUpdatePreferences.autoUpdate = false
        stopService(Intent(this, WidgetRefreshService::class.java))
        finish()
    }
}
