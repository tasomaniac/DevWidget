package com.tasomaniac.devwidget.data

import io.reactivex.Completable

fun FilterDao.insertPackageMatchers(appWidgetId: Int, packageMatchers: List<String>) =
    Completable.fromAction {
      val filters = packageMatchers.map {
        Filter(it, appWidgetId)
      }
      insertFilterSync(filters)
    }

fun FilterDao.deletePackageMatcher(packageMatcher: String) =
    Completable.fromAction {
      deleteFilterSync(packageMatcher)
    }
