package com.tasomaniac.devdrawer.main

import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.widget.WidgetDataResolver
import io.reactivex.Flowable
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val dao: Dao,
    private val widgetDataResolver: WidgetDataResolver) {

  fun observeWidgets(): Flowable<List<WidgetListData>> {
    return dao.allWidgetsFlowable()
        .map { widgets ->
          widgets.map {
            val widgetData = it.packageNames
                .mapNotNull(widgetDataResolver::resolve)
            WidgetListData(Widget(it.appWidgetId, it.name), widgetData)
          }
        }
  }
}
