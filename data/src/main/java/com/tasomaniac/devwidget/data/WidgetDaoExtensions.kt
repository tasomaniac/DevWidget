package com.tasomaniac.devwidget.data

import io.reactivex.Completable

fun WidgetDao.updateTempWidgetId(appWidgetId: Int) =
    Completable.fromAction {
        updateTempWidgetIdSync(appWidgetId)
    }

fun WidgetDao.deleteWidgets(widgets: List<Widget>) =
    Completable.fromAction {
        deleteWidgetsSync(widgets)
    }
