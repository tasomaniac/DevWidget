package com.tasomaniac.devwidget.configure

import com.tasomaniac.devwidget.widget.preview.WidgetListData

internal interface ConfigureView {
    fun updateWidgetPreview(widgetListData: WidgetListData)
    fun setFilters(filters: List<String>)
    fun setItems(items: Collection<String>)
    fun finishWith(appWidgetId: Int)

    var onConfirmClicked: () -> Unit
    var onSettingsClicked: () -> Unit
    var onPackageMatcherAdded: (packageMatcher: String) -> Unit
}
