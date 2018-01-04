package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Widget

object WidgetUpdater {

  fun update(context: Context, widget: Widget) {
    val remoteViews = RemoteViews(context.packageName, R.layout.app_widget)

    remoteViews.setTextViewText(R.id.widgetTitle, widget.name)
    remoteViews.setRemoteAdapter(R.id.widgetAppList, remoteAdapter(context, widget.appWidgetId))

    AppWidgetManager.getInstance(context).updateAppWidget(widget.appWidgetId, remoteViews)
  }

  private fun remoteAdapter(context: Context, appWidgetId: Int): Intent {
    return Intent(context, WidgetViewsService::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }
  }
}
