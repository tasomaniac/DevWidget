package com.tasomaniac.devdrawer.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.configure.ConfigureActivity
import com.tasomaniac.devdrawer.data.Widget
import javax.inject.Inject

class WidgetUpdater @Inject constructor(
    private val app: Application,
    private val appWidgetManager: AppWidgetManager
) {

  fun update(widget: Widget) {
    val remoteViews = RemoteViews(app.packageName, R.layout.app_widget).apply {
      if (widget.name.isEmpty()) {
        setViewVisibility(R.id.widgetHeader, View.GONE)
      } else {
        setViewVisibility(R.id.widgetHeader, View.VISIBLE)
        setTextViewText(R.id.widgetTitle, widget.name)

        setupConfigureButton(widget)
      }

      setRemoteAdapter(R.id.widgetAppList, remoteAdapter(app, widget.appWidgetId))
      setPendingIntentTemplate(R.id.widgetAppList, ClickHandlingActivity.intent(app).toPending(app))
    }

    appWidgetManager.updateAppWidget(widget.appWidgetId, remoteViews)
  }

  private fun RemoteViews.setupConfigureButton(widget: Widget) {
    setContentDescription(R.id.widgetConfigure,
        app.getString(R.string.widget_content_description_configure, widget))
    val intent = ConfigureActivity.createIntent(app, widget.appWidgetId).toPending(app)
    setOnClickPendingIntent(R.id.widgetConfigure, intent)
  }

  fun notifyWidgetDataChanged(appWidgetId: Int) {
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
  }

  companion object {

    private fun remoteAdapter(context: Context, appWidgetId: Int): Intent {
      return Intent(context, WidgetViewsService::class.java).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
      }
    }

    private fun Intent.toPending(context: Context) =
        PendingIntent.getActivity(context, 0, this, 0)
  }
}
