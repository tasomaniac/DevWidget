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

fun Dao.insertApp(appWidgetId: Int, packageMatcher: String, packageName: String) =
    insertApps(appWidgetId, packageMatcher, listOf(packageName))

fun Dao.insertApps(appWidgetId: Int, packageMatcher: String, packageNames: List<String>): Completable =
    Completable.fromAction {
      val apps = packageNames.map {
        App(it, packageMatcher, appWidgetId)
      }
      insertAppSync(apps)
    }

fun Dao.deleteWidgets(vararg widgets: Widget): Completable = Completable.fromAction {
  deleteWidgetsSync(*widgets)
}

fun Dao.deleteApp(packageName: String): Completable = Completable.fromAction {
  deleteAppSync(packageName)
}
