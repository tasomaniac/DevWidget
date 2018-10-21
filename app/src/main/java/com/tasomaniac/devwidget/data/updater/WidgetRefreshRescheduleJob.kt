package com.tasomaniac.devwidget.data.updater

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetRefreshRescheduleJob : JobService() {

    @Inject lateinit var shouldStartWidgetRefreshService: ShouldStartWidgetRefreshService

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (shouldStartWidgetRefreshService.check()) {
            startForegroundService(Intent(this, WidgetRefreshService::class.java))
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}
