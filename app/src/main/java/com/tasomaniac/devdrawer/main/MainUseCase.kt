package com.tasomaniac.devdrawer.main

import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.rx.flatten
import com.tasomaniac.devdrawer.widget.WidgetDataResolver
import io.reactivex.Flowable
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val dao: Dao,
    private val widgetDataResolver: WidgetDataResolver) {

  fun observeWidgets(): Flowable<WidgetListData> {
    return dao.allWidgetsFlowable()
        .flatten()
        .flatMap { widget ->
          dao.findAppsByWidgetId(widget.appWidgetId)
              .map { it.mapNotNull(widgetDataResolver::resolve) }
              .map { WidgetListData(widget, it) }
        }
  }
}
