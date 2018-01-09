package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun FilterDao.insertFilters(appWidgetId: Int, packageMatchers: List<String>): Completable =
    Completable.fromAction {
      val filters = packageMatchers.map {
        Filter(it, appWidgetId)
      }
      insertFilterSync(filters)
    }

fun FilterDao.deleteFilter(packageMatcher: String): Completable =
    Completable.fromAction {
      deleteFilterSync(packageMatcher)
    }
