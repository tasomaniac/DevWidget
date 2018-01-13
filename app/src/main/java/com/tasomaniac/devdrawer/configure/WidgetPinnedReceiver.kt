package com.tasomaniac.devdrawer.configure

import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.data.updateTempWidgetId
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.Completable
import javax.inject.Inject

class WidgetPinnedReceiver : DaggerBroadcastReceiver() {

  @Inject lateinit var widgetDao: WidgetDao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var widgetUpdater: WidgetUpdater

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)

    widgetDao.updateTempWidgetId(intent.appWidgetId)
        .andThen(widgetDao.findWidgetById(intent.appWidgetId))
        .flatMapCompletable { it.update() }
        .compose(scheduling.forCompletable())
        .subscribe()
  }

  private fun Widget.update() =
      Completable.fromAction {
        widgetUpdater.update(this)
      }

  private val Intent.appWidgetId
    get() = getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
}
