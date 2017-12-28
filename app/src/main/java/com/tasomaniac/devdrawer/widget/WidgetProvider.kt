package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R

class WidgetProvider : AppWidgetProvider() {

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    appWidgetIds.forEach { id -> updateAppWidget(context, appWidgetManager, id) }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    TODO("Delete associated persisted data")
  }

  companion object {

    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      val remoteViews = RemoteViews(context.packageName, R.layout.app_widget)

      val widgetText = "Some text" // TODO
      remoteViews.setTextViewText(R.id.widgetTitle, widgetText)
      remoteViews.setRemoteAdapter(R.id.widgetAppList, remoteAdapter(context, appWidgetId))

      appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun remoteAdapter(context: Context, appWidgetId: Int): Intent {
      return Intent(context, WidgetViewsService::class.java).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
      }
    }
  }
}

