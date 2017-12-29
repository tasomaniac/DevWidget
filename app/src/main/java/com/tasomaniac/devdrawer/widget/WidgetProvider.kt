package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.deleteWidgets
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    super.onReceive(context, intent)
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    appWidgetIds.forEach { id ->
      updateAppWidget(context, appWidgetManager, id)
    }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    val widgets = appWidgetIds.map { Widget(it) }
    appDao.deleteWidgets(*widgets.toTypedArray())
        .compose(scheduling.forCompletable())
        .subscribe()
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

