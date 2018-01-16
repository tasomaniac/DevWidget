package com.tasomaniac.devwidget.main

import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.data.WidgetDao
import com.tasomaniac.devwidget.settings.Sorting.*
import com.tasomaniac.devwidget.settings.SortingPreferences
import com.tasomaniac.devwidget.widget.WidgetData
import com.tasomaniac.devwidget.widget.WidgetDataResolver
import io.reactivex.Flowable
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val widgetDao: WidgetDao,
    private val widgetDataResolver: WidgetDataResolver,
    private val sortingPreferences: SortingPreferences) {

  fun observeWidgets(): Flowable<List<WidgetListData>> {
    return widgetDao.allWidgetsWithPackages()
        .map { widgets ->
          widgets.map {
            val widgetData = it.packageNames
                .mapNotNull(widgetDataResolver::resolve)
                .sort()
            WidgetListData(Widget(it.appWidgetId, it.name), widgetData)
          }
        }
  }

  private fun List<WidgetData>.sort() = when (sortingPreferences.sorting) {
    ORDER_ADDED -> asReversed()
    ALPHABETICALLY_PACKAGES -> sortedBy { it.packageName }
    ALPHABETICALLY_NAMES -> sortedBy { it.label }
  }

}
