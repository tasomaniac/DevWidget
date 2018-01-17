package com.tasomaniac.devwidget.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build.VERSION_CODES.O
import android.support.annotation.RequiresApi
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.configure.WidgetPinnedReceiver
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.rx.flatten
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class WidgetUpdater @Inject constructor(
    private val app: Application,
    private val appWidgetManager: AppWidgetManager,
    private val removeViewsCreator: RemoveViewsCreator,
    private val widgetDao: WidgetDao
) {

    @RequiresApi(O)
    fun requestPin() {
        val widgetProvider = ComponentName(app, WidgetProvider::class.java)
        val successCallback = Intent(app, WidgetPinnedReceiver::class.java)
            .toPendingBroadcast(app)

        appWidgetManager.requestPinAppWidget(widgetProvider, null, successCallback)
    }

    @CheckReturnValue
    fun update(widget: Widget) =
        Completable.fromAction {
            val remoteViews = removeViewsCreator.create(widget)
            appWidgetManager.updateAppWidget(widget.appWidgetId, remoteViews)
        }

    @CheckReturnValue
    fun updateAll() =
        widgetDao.allWidgets()
            .flatten()
            .flatMapCompletable {
                update(it)
                    .andThen(Completable.fromAction {
                        appWidgetManager.notifyAppWidgetViewDataChanged(
                            it.appWidgetId,
                            R.id.widgetAppList
                        )
                    })
            }

    fun notifyWidgetDataChanged(appWidgetId: Int) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
    }

}
