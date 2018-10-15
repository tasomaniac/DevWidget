package com.tasomaniac.devwidget.configure

import androidx.lifecycle.ViewModel
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.extensions.Debouncer
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.extensions.onlyTrue
import com.tasomaniac.devwidget.widget.WidgetUpdater
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class WidgetNameModel @Inject constructor(
    private val widgetDao: WidgetDao,
    private val widgetUpdater: WidgetUpdater,
    val appWidgetId: Int,
    debouncer: Debouncer<String>,
    scheduling: SchedulingStrategy
) : ViewModel() {

    var newWidget = false
    private val disposable: Disposable
    private val processor = BehaviorProcessor.create<String>()

    init {
        disposable = insertIfNotFound()
            .andThen(
                processor
                    .distinctUntilChanged()
                    .compose(debouncer)
                    .flatMapCompletable(::updateWidget)
            )
            .compose(scheduling.forCompletable())
            .subscribe {
                // no-op
            }
    }

    private fun insertIfNotFound() =
        findWidget()
            .isEmpty.onlyTrue()
            .doOnSuccess { newWidget = true }
            .flatMapCompletable {
                widgetDao.insertWidget(Widget(appWidgetId))
            }

    private fun updateWidget(widgetName: String): Completable {
        val widget = Widget(appWidgetId, widgetName)
        return widgetDao.updateWidget(widget)
            .andThen(widgetUpdater.update(widget))
    }

    fun updateWidgetName(widgetName: String) {
        processor.onNext(widgetName)
    }

    @CheckReturnValue
    fun currentWidgetName() = findWidget().map(Widget::name)

    private fun findWidget() = widgetDao.findWidgetById(appWidgetId)

    val data get() = processor.hide()

    override fun onCleared() = disposable.dispose()
}
