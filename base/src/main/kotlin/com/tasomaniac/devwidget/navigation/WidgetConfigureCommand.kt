package com.tasomaniac.devwidget.navigation

import android.net.Uri

fun widgetConfigureCommand(appWidgetId: Int) =
    UriCommand(Uri.parse("devwidget://configure?appWidgetId=$appWidgetId"))
