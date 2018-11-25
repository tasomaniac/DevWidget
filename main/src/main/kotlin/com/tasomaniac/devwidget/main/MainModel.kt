package com.tasomaniac.devwidget.main

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetAppDao
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_NAMES
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_PACKAGES
import com.tasomaniac.devwidget.settings.Sorting.ORDER_ADDED
import com.tasomaniac.devwidget.settings.SortingPreferences
import com.tasomaniac.devwidget.widget.ApplicationInfoResolver
import com.tasomaniac.devwidget.widget.DisplayApplicationInfo
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

private typealias MainResult = Pair<List<WidgetListData>, DiffUtil.DiffResult>

internal class MainModel @Inject constructor(
    private val sortingPreferences: SortingPreferences,
    applicationInfoResolver: ApplicationInfoResolver,
    widgetAppDao: WidgetAppDao
) : ViewModel() {

    private val processor = BehaviorProcessor.create<MainResult>()
    private val disposable: Disposable

    init {
        disposable = widgetAppDao.allWidgetsWithPackages()
            .map { widgets ->
                widgets.map {
                    val apps = it.packageNames
                        .flatMap(applicationInfoResolver::resolve)
                        .sort()
                    WidgetListData(Widget(it.appWidgetId, it.name), apps)
                }
            }
            .scan(INITIAL_PAIR) { (data, _), newData ->
                newData to DiffUtil.calculateDiff(WidgetDiffCallbacks(data, newData))
            }
            .skip(1)
            .subscribe(processor::onNext)
    }

    val data get() = processor.hide()

    override fun onCleared() = disposable.dispose()

    private fun List<DisplayApplicationInfo>.sort() = when (sortingPreferences.sorting) {
        ORDER_ADDED -> asReversed()
        ALPHABETICALLY_PACKAGES -> sortedBy { it.packageName }
        ALPHABETICALLY_NAMES -> sortedBy { it.label.toString() }
    }

    companion object {

        private val INITIAL_PAIR = emptyList<WidgetListData>() to
                DiffUtil.calculateDiff(WidgetDiffCallbacks(emptyList(), emptyList()))
    }
}
