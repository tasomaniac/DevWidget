package com.tasomaniac.devdrawer.main

import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.settings.SortingPreferences
import com.tasomaniac.devdrawer.settings.SortingPreferences.Sorting.*
import com.tasomaniac.devdrawer.widget.WidgetData
import com.tasomaniac.devdrawer.widget.WidgetDataResolver
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
