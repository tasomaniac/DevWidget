package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.extensions.flatten
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WidgetUpdaterImpl @Inject constructor(
    override val appWidgetManager: AppWidgetManager,
    private val removeViewsCreatorFactory: RemoveViewsCreator.Factory,
    private val widgetDao: WidgetDao
) : WidgetUpdater {

    @CheckReturnValue
    override fun update(widget: Widget, widgetOptions: Bundle) =
        Completable.fromAction {
            val minWidth = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val remoteViews = removeViewsCreatorFactory.create(widget, minWidth).create()
            appWidgetManager.updateAppWidget(widget.appWidgetId, remoteViews)
        }

    @Suppress("MagicNumber")
    @CheckReturnValue
    override fun updateAll() =
        widgetDao.allWidgets()
            .flatten()
            .flatMapCompletable { widget ->
                update(widget)
                    .delay(300, TimeUnit.MILLISECONDS)
                    .andThen(Completable.fromAction {
                        notifyWidgetDataChanged(widget.appWidgetId)
                    })
            }

    override fun notifyWidgetDataChanged(appWidgetId: Int) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
    }
}
