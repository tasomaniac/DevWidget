package com.tasomaniac.devwidget.widget.preview

import com.tasomaniac.devwidget.data.Action
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.widget.DisplayApplicationInfo

data class WidgetListData(
    val widget: Widget,
    val apps: List<DisplayApplicationInfo>,
    val favAction: Action = Action.UNINSTALL
)
