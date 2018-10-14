package com.tasomaniac.devwidget.receivers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.data.deleteApp
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.widget.WidgetUpdater
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

class PackageRemovedReceiver : DaggerBroadcastReceiver() {

    @Inject lateinit var widgetDao: WidgetDao
    @Inject lateinit var appDao: AppDao
    @Inject lateinit var scheduling: SchedulingStrategy
    @Inject lateinit var widgetUpdater: WidgetUpdater

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != Intent.ACTION_PACKAGE_REMOVED) {
            throw IllegalStateException("Unexpected receiver with action: ${intent.action}")
        }

        val pendingResult = goAsync()
        val uninstalledPackage = intent.data!!.schemeSpecificPart

        appDao.deleteApp(uninstalledPackage)
            .andThen(widgetUpdater.updateAll())
            .compose(scheduling.forCompletable())
            .subscribe {
                pendingResult.finish()
            }
    }
}
