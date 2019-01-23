package com.tasomaniac.devwidget.data

fun FavActionDao.findFavActionByWidgetIdSync(appWidgetId: Int) =
    _findFavActionByWidgetIdSync(appWidgetId) ?: Action.UNINSTALL

fun FavActionDao.findFavActionByWidgetId(appWidgetId: Int) =
    _findFavActionByWidgetId(appWidgetId).toSingle(Action.UNINSTALL)
