package com.tasomaniac.devwidget.configure

import android.view.View
import com.tasomaniac.devwidget.settings.OpacityPreferences
import com.tasomaniac.devwidget.widget.WidgetResources
import com.tasomaniac.devwidget.widget.preview.WidgetListData
import com.tasomaniac.devwidget.widget.preview.WidgetView
import javax.inject.Inject

internal class WidgetPreview @Inject constructor(
    private val widgetViewFactory: WidgetView.Factory,
    private val widgetResources: WidgetResources,
    private val opacityPreferences: OpacityPreferences
) {

    fun updateWidgetPreview(widgetPreview: View, widgetListData: WidgetListData) {
        val shadeColor = widgetResources.resolveBackgroundColor(opacityPreferences.opacity)
        widgetPreview.setBackgroundColor(shadeColor)
        widgetViewFactory.createWith(widgetPreview).bind(widgetListData)
    }
}
