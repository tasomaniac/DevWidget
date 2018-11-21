package com.tasomaniac.devwidget.main

import android.content.res.Resources
import com.tasomaniac.devwidget.data.Widget
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
