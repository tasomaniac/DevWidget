package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun WidgetDao.insertWidget(widget: Widget): Completable =
    Completable.fromAction {
      insertWidgetSync(widget)
    }

fun WidgetDao.updateWidget(widget: Widget): Completable =
    Completable.fromAction {
      updateWidgetSync(widget)
    }

fun WidgetDao.deleteWidgets(vararg widgets: Widget): Completable = Completable.fromAction {
  deleteWidgetsSync(*widgets)
}
