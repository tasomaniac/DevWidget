package com.tasomaniac.devwidget.configure

import androidx.lifecycle.ViewModel
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.extensions.onlyTrue
import com.tasomaniac.devwidget.widget.WidgetUpdater
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import javax.inject.Inject

internal class WidgetNameModel @Inject constructor(
    private val widgetDao: WidgetDao,
    private val widgetUpdater: WidgetUpdater,
    val appWidgetId: Int,
    scheduling: SchedulingStrategy
) : ViewModel() {

    private val disposable: Disposable

    init {
        disposable = insertIfNotFound()
            .andThen(widgetUpdater.updateAll())
            .compose(scheduling.forCompletable())
            .subscribe()
    }

    private fun insertIfNotFound() =
        findWidget()
            .isEmpty
            .onlyTrue()
            .flatMapCompletable {
                widgetDao.insertWidget(Widget(appWidgetId))
            }

    @CheckReturnValue
    fun updateWidgetName(widgetName: String): Completable {
        val widget = Widget(appWidgetId, widgetName)
        return widgetDao.updateWidget(widget)
            .andThen(widgetUpdater.update(appWidgetId, widgetName))
    }

    @CheckReturnValue
    fun currentWidgetName() = findWidget().map(Widget::name)

    private fun findWidget() = widgetDao.findWidgetById(appWidgetId)

    override fun onCleared() = disposable.dispose()
}
