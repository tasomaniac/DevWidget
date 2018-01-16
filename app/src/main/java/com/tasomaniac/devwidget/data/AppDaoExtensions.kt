package com.tasomaniac.devwidget.data

import io.reactivex.Completable

fun AppDao.insertApp(appWidgetId: Int, packageMatcher: String, packageName: String) =
    insertApps(appWidgetId, packageMatcher, listOf(packageName))

fun AppDao.insertApps(appWidgetId: Int, packageMatcher: String, packageNames: List<String>) =
    Completable.fromAction {
      val apps = packageNames.map {
        App(it, packageMatcher, appWidgetId)
      }
      insertAppSync(apps)
    }

fun AppDao.deleteApp(packageName: String) =
    Completable.fromAction {
      deleteAppSync(packageName)
    }

fun AppDao.deleteAppsByPackageMatcher(packageMatcher: String) =
    Completable.fromAction {
      deleteAppsByPackageMatcherSync(packageMatcher)
    }
