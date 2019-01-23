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
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

internal class WidgetNameModel @Inject constructor(
    private val widgetDao: WidgetDao,
    private val widgetUpdater: WidgetUpdater,
    val appWidgetId: Int,
    private val scheduling: SchedulingStrategy
) : ViewModel() {

    private val disposable: Disposable
    private val processor = BehaviorProcessor.create<String>()

    init {
        disposable = insertIfNotFound()
            .andThen(
                processor.flatMapCompletable(::updateWidget)
            )
            .compose(scheduling.forCompletable())
            .subscribe()
    }

    private fun insertIfNotFound() =
        findWidget()
            .isEmpty.onlyTrue()
            .flatMapCompletable {
                widgetDao.insertWidget(Widget(appWidgetId))
            }

    private fun updateWidget(widgetName: String): Completable {
        val widget = Widget(appWidgetId, widgetName)
        return widgetDao.updateWidget(widget)
            .andThen(widgetUpdater.update(appWidgetId, widgetName))
            .compose(scheduling.forCompletable())
    }

    fun updateWidgetName(widgetName: String) {
        processor.onNext(widgetName)
    }

    @CheckReturnValue
    fun currentWidgetName() = findWidget().map(Widget::name)

    private fun findWidget() = widgetDao.findWidgetById(appWidgetId)

    override fun onCleared() = disposable.dispose()
}
