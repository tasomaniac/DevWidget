package com.tasomaniac.devwidget.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.extensions.flatten
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WidgetUpdaterImpl @Inject constructor(
    override val appWidgetManager: AppWidgetManager,
    private val app: Application,
    private val remoteViewsCreatorFactory: RemoteViewsCreator.Factory,
    private val widgetDao: WidgetDao
) : WidgetUpdater {

    @CheckReturnValue
    override fun update(appWidgetId: Int, name: String, widgetOptions: Bundle) =
        Completable.fromAction {
            val widget = Widget(appWidgetId, name)
            val minWidth = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val remoteViews = remoteViewsCreatorFactory.create(widget, minWidth).create()
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

    @Suppress("MagicNumber")
    @CheckReturnValue
    override fun updateAll() =
        widgetDao.allWidgets()
            .flatten()
            .flatMapCompletable { widget ->
                update(widget.appWidgetId, widget.name)
                    .delay(300, TimeUnit.MILLISECONDS)
                    .andThen(Completable.fromAction {
                        notifyWidgetDataChanged(widget.appWidgetId)
                    })
            }

    override fun notifyWidgetDataChanged(appWidgetId: Int) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
    }

    override fun hasWidgets() =
        appWidgetManager.getAppWidgetIds(ComponentName(app, WidgetProvider::class.java)).isNotEmpty()
}
