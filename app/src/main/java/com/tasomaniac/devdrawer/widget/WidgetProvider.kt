package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.data.deleteWidgets
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.rx.flatten
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

  @Inject lateinit var widgetDao: WidgetDao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var widgetUpdater: WidgetUpdater

  private var disposable: Disposable = Disposables.empty()

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    super.onReceive(context, intent)
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, vararg appWidgetIds: Int) {
    if (appWidgetIds.isEmpty()) return

    val pendingResult = goAsync()

    disposable.dispose()
    disposable = widgetDao.findWidgetsById(*appWidgetIds)
        .flatten()
        .flatMapCompletable(widgetUpdater::update)
        .compose(scheduling.forCompletable())
        .subscribe {
          pendingResult.finish()
        }
  }

  override fun onDeleted(context: Context, vararg appWidgetIds: Int) {
    val widgets = appWidgetIds.map { Widget(it) }
    widgetDao.deleteWidgets(widgets)
        .compose(scheduling.forCompletable())
        .subscribe()
  }
}

