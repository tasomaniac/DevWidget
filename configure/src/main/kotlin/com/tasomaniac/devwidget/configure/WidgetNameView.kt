package com.tasomaniac.devwidget.configure

internal interface WidgetNameView {
    var widgetNameChanged: (widgetName: String) -> Unit
}
