package com.tasomaniac.devwidget.configure

interface ConfigureView {
    fun setFilters(filters: List<String>)
    fun setItems(items: Collection<String>)
    fun setListener(listener: Listener?)
    fun finishWith(appWidgetId: Int)

    interface Listener {
        fun onConfirmClicked()
        fun onPackageMatcherAdded(packageMatcher: String)
    }

}
