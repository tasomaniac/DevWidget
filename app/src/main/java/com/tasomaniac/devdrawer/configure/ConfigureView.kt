package com.tasomaniac.devdrawer.configure

interface ConfigureView {
  fun setWidgetName(widgetName: String)
  fun setItems(items: Collection<String>)
  fun setListener(listener: Listener?)
  fun finishWith(appWidgetId: Int)

  interface Listener {
    fun onAddWidgetClicked(widgetName: String, filters: List<String>)
  }

}
