package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun Dao.insertWidget(widget: Widget): Completable =
    Completable.fromAction {
      insertWidgetSync(widget)
    }

fun Dao.updateWidget(widget: Widget): Completable =
    Completable.fromAction {
      updateWidgetSync(widget)
    }

fun Dao.insertApps(appWidgetId: Int, vararg packageNames: String) =
    insertApps(appWidgetId, packageNames.toList())

fun Dao.insertApps(appWidgetId: Int, packageNames: List<String>): Completable =
    Completable.fromAction {
      val apps = packageNames.map {
        App(it, appWidgetId)
      }
      insertAppSync(apps)
    }

fun Dao.deleteWidgets(vararg widgets: Widget): Completable = Completable.fromAction {
  deleteWidgetsSync(*widgets)
}

fun Dao.deleteApp(packageName: String): Completable = Completable.fromAction {
  deleteAppSync(packageName)
}
