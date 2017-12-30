package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun Dao.insert(appWidgetId: Int, vararg packageNames: String): Completable = Completable.fromAction {
  insertWidgetSync(Widget(appWidgetId))
  packageNames.forEach {
    insertSync(App(it, appWidgetId))
  }
}

fun Dao.deleteWidgets(vararg widgets: Widget): Completable = Completable.fromAction {
  deleteWidgetsSync(*widgets)
}
