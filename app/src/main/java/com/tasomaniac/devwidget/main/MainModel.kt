package com.tasomaniac.devwidget.main

import android.arch.lifecycle.ViewModel
import android.support.v7.util.DiffUtil
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetAppDao
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import com.tasomaniac.devwidget.settings.Sorting.*
import com.tasomaniac.devwidget.settings.SortingPreferences
import com.tasomaniac.devwidget.widget.WidgetData
import com.tasomaniac.devwidget.widget.WidgetDataResolver
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

private typealias MainResult = Pair<List<WidgetListData>, DiffUtil.DiffResult>

class MainModel @Inject constructor(
    private val sortingPreferences: SortingPreferences,
    widgetDataResolver: WidgetDataResolver,
    widgetAppDao: WidgetAppDao,
    scheduling: SchedulingStrategy
) : ViewModel() {

    private val processor = BehaviorProcessor.create<MainResult>()
    private val disposable: Disposable

    init {
        disposable = widgetAppDao.allWidgetsWithPackages()
            .map { widgets ->
                widgets.map {
                    val widgetData = it.packageNames
                        .mapNotNull(widgetDataResolver::resolve)
                        .sort()
                    WidgetListData(Widget(it.appWidgetId, it.name), widgetData)
                }
            }
            .scan(INITIAL_PAIR) { (data, _), newData ->
                newData to DiffUtil.calculateDiff(WidgetDiffCallbacks(data, newData))
            }
            .skip(1)
            .compose(scheduling.forFlowable())
            .subscribe(processor::onNext)
    }

    val data get() = processor.hide()

    override fun onCleared() = disposable.dispose()

    private fun List<WidgetData>.sort() = when (sortingPreferences.sorting) {
        ORDER_ADDED -> asReversed()
        ALPHABETICALLY_PACKAGES -> sortedBy { it.packageName }
        ALPHABETICALLY_NAMES -> sortedBy { it.label }
    }

    companion object {

        private val INITIAL_PAIR = emptyList<WidgetListData>() to
                DiffUtil.calculateDiff(WidgetDiffCallbacks(emptyList(), emptyList()))
    }
}
