package com.tasomaniac.devwidget.configure

internal interface ConfigureView {
    fun setWidgetName(widgetName: String)
    fun setFilters(filters: List<String>)
    fun setItems(items: Collection<String>)
    fun finishWith(appWidgetId: Int)

    var onConfirmClicked: () -> Unit
    var widgetNameChanged: (widgetName: String) -> Unit
    var onPackageMatcherAdded: (packageMatcher: String) -> Unit
}
