package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun Dao.insert(widget: Widget, vararg packageNames: String): Completable = Completable.fromAction {
  insertWidgetSync(widget)
  packageNames.forEach {
    insertSync(App(it, widget.appWidgetId))
  }
}

fun Dao.deleteWidgets(vararg widgets: Widget): Completable = Completable.fromAction {
  deleteWidgetsSync(*widgets)
}
