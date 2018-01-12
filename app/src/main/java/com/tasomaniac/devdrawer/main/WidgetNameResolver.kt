package com.tasomaniac.devdrawer.main

import android.content.res.Resources
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Widget
import javax.inject.Inject

class WidgetNameResolver @Inject constructor(private val resources: Resources) {

  fun resolve(widget: Widget): String {
    return if (widget.name.isEmpty()) {
      resources.getString(R.string.widget_empty_title, widget.appWidgetId)
    } else {
      widget.name
    }
  }
}
