package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.data.deleteWidgets
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import com.tasomaniac.devwidget.rx.flatten
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

    @Inject lateinit var widgetDao: WidgetDao
    @Inject lateinit var scheduling: SchedulingStrategy
    @Inject lateinit var widgetUpdater: WidgetUpdater

    private val disposables = CompositeDisposable()

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, vararg appWidgetIds: Int) {
        if (appWidgetIds.isEmpty()) return

        val pendingResult = goAsync()

        disposables.clear()
        disposables.add(widgetDao.findWidgetsById(appWidgetIds)
            .flatten()
            .flatMapCompletable {
                widgetUpdater.update(it)
            }
            .compose(scheduling.forCompletable())
            .subscribe(pendingResult::finish))
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int, newOptions: Bundle
    ) {
        val pendingResult = goAsync()

        disposables.clear()
        disposables.add(widgetDao.findWidgetById(appWidgetId)
            .flatMapCompletable {
                widgetUpdater.update(it, newOptions)
            }
            .compose(scheduling.forCompletable())
            .subscribe(pendingResult::finish))
    }

    override fun onDeleted(context: Context, vararg appWidgetIds: Int) {
        val widgets = appWidgetIds.map { Widget(it) }
        widgetDao.deleteWidgets(widgets)
            .compose(scheduling.forCompletable())
            .subscribe()
    }
}

