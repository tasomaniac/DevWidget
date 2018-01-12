package com.tasomaniac.devdrawer.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Widget
import javax.inject.Inject

class WidgetUpdater @Inject constructor(
    private val app: Application,
    private val appWidgetManager: AppWidgetManager,
    private val widgetNameResolver: WidgetNameResolver
) {

  fun update(widget: Widget) {
    val remoteViews = RemoteViews(app.packageName, R.layout.app_widget)

    remoteViews.setTextViewText(R.id.widgetTitle, widgetNameResolver.resolve(widget))
    remoteViews.setRemoteAdapter(R.id.widgetAppList, remoteAdapter(app, widget.appWidgetId))

    appWidgetManager.updateAppWidget(widget.appWidgetId, remoteViews)
  }

  fun notifyWidgetDataChanged(appWidgetId: Int) {
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
  }

  private fun remoteAdapter(context: Context, appWidgetId: Int): Intent {
    return Intent(context, WidgetViewsService::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }
  }
}
