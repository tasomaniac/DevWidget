package com.tasomaniac.devwidget.navigation

fun widgetConfigureCommand(appWidgetId: Int) =
    UriCommand("devwidget://configure?appWidgetId=$appWidgetId")

fun settingsCommand() = UriCommand("devwidget://settings")
