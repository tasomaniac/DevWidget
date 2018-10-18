package com.tasomaniac.devwidget.data.updater

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devwidget.settings.AutoUpdatePreferences
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetRefreshRescheduleJob : JobService() {

    @Inject lateinit var autoUpdatePreferences: AutoUpdatePreferences

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (SDK_INT >= O && autoUpdatePreferences.autoUpdate) {
            startForegroundService(Intent(this, WidgetRefreshService::class.java))
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}
