package com.tasomaniac.devdrawer.data

import io.reactivex.Completable

fun AppDao.insert(app: App): Completable = Completable.fromAction {
  insertSync(app)
}

fun AppDao.deleteWidgets(vararg appWidgetIds: Int): Completable = Completable.fromAction {
  deleteWidgetsSync(*appWidgetIds)
}
