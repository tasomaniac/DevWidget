package com.tasomaniac.devdrawer.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.O
import android.support.annotation.IdRes
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.configure.ConfigureActivity
import com.tasomaniac.devdrawer.configure.WidgetPinnedReceiver
import com.tasomaniac.devdrawer.data.Widget
import io.reactivex.Completable
import javax.inject.Inject

class WidgetUpdater @Inject constructor(
    private val app: Application,
    private val appWidgetManager: AppWidgetManager
) {

  @RequiresApi(O)
  fun requestPin() {
    val widgetProvider = ComponentName(app, WidgetProvider::class.java)
    val successCallback = Intent(app, WidgetPinnedReceiver::class.java)
        .toPendingBroadcast(app)

    appWidgetManager.requestPinAppWidget(widgetProvider, null, successCallback)
  }

  fun update(widget: Widget) = Completable.fromAction {
    RemoveViewsCreator(widget)
        .createAndUpdate()
  }

  fun notifyWidgetDataChanged(appWidgetId: Int) {
    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
  }

  private inner class RemoveViewsCreator(private val widget: Widget) {

    fun createAndUpdate() {
      appWidgetManager.updateAppWidget(widget.appWidgetId, create())
    }

    private fun create() = RemoteViews(app.packageName, R.layout.app_widget).apply {
      if (widget.name.isEmpty()) {
        setViewVisibility(R.id.widgetHeader, View.GONE)
      } else {
        setViewVisibility(R.id.widgetHeader, View.VISIBLE)
        setTextViewText(R.id.widgetTitle, widget.name)

        setupConfigureButton(R.id.widgetConfigure)
      }

      setRemoteAdapter(R.id.widgetAppList, remoteAdapter(app))

      setEmptyView(R.id.widgetAppList, R.id.widgetEmpty)
      setupConfigureButton(R.id.widgetEmpty)

      val intentTemplate = ClickHandlingActivity.intent(app).toPendingActivity(app)
      setPendingIntentTemplate(R.id.widgetAppList, intentTemplate)
    }

    private fun RemoteViews.setupConfigureButton(@IdRes buttonId: Int) {
      setContentDescription(buttonId, app.getString(R.string.widget_content_description_configure, widget))
      val intent = ConfigureActivity.createIntent(app, widget.appWidgetId)
          .toPendingActivity(app)
      setOnClickPendingIntent(buttonId, intent)
    }

    private fun remoteAdapter(context: Context): Intent {
      return Intent(context, WidgetViewsService::class.java).apply {
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widget.appWidgetId)
        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
      }
    }
  }

  companion object {

    private fun Intent.toPendingActivity(context: Context) =
        PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)

    private fun Intent.toPendingBroadcast(context: Context) =
        PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
  }
}
