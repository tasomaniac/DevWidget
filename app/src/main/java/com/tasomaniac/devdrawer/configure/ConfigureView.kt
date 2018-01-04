package com.tasomaniac.devdrawer.configure

import com.tasomaniac.devdrawer.data.Widget

interface ConfigureView {
  fun setWidgetName(widgetName: String)
  fun setFilters(filters: List<String>)
  fun setItems(items: Collection<String>)
  fun updateWidget(widget: Widget)
  fun setListener(listener: Listener?)
  fun finishWith(appWidgetId: Int)

  interface Listener {
    fun onAddWidgetClicked(widgetName: String, filters: List<String>)
    fun widgetNameChanged(widgetName: String)
  }

}
