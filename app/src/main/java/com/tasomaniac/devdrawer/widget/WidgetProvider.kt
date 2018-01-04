package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.deleteWidgets
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.rx.flatten
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager

  private var disposable: Disposable = Disposables.empty()

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    super.onReceive(context, intent)
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    if (appWidgetIds.isEmpty()) return

    disposable.dispose()
    disposable = dao.findWidgetsById(*appWidgetIds)
        .flatten()
        .compose(scheduling.forObservable())
        .subscribe {
          WidgetUpdater.update(context, it)
        }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    val widgets = appWidgetIds.map { Widget(it) }
    dao.deleteWidgets(*widgets.toTypedArray())
        .compose(scheduling.forCompletable())
        .subscribe()
  }
}

