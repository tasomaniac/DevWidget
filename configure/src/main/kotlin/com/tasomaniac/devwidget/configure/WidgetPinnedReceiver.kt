package com.tasomaniac.devwidget.configure

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Context
import android.content.Intent
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.data.updateTempWidgetId
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.widget.WidgetUpdater
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

internal class WidgetPinnedReceiver : DaggerBroadcastReceiver() {

    @Inject lateinit var widgetDao: WidgetDao
    @Inject lateinit var scheduling: SchedulingStrategy
    @Inject lateinit var widgetUpdater: WidgetUpdater

    @SuppressLint("CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val pendingResult = goAsync()
        widgetDao.updateTempWidgetId(intent.appWidgetId)
            .andThen(widgetDao.findWidgetById(intent.appWidgetId))
            .flatMapCompletable { (appWidgetId, widgetName) ->
                widgetUpdater.update(appWidgetId, widgetName)
            }
            .compose(scheduling.forCompletable())
            .subscribe {
                pendingResult.finish()
            }
    }

    private val Intent.appWidgetId
        get() = getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
}
