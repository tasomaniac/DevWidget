package com.tasomaniac.devwidget.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.extensions.inflate
import com.tasomaniac.devwidget.widget.WidgetData
import com.tasomaniac.devwidget.widget.WidgetResources
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.app_widget_list_item.*
import javax.inject.Inject

class AppViewHolder(
    private val widgetResources: WidgetResources,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(widgetData: WidgetData) {
        appWidgetContainer.background = null

        appWidgetIcon.setImageBitmap(widgetData.icon)
        appWidgetLabel.text = widgetData.label
        appWidgetLabel.setTextColor(widgetResources.foregroundColor)
        appWidgetPackageName.text = widgetData.packageName
        appWidgetPackageName.setTextColor(widgetResources.foregroundColor)

        appWidgetUninstall.setImageResource(widgetResources.deleteIcon)
        appWidgetDetails.setImageResource(widgetResources.settingsIcon)
    }

    class Factory @Inject constructor(widgetResources: WidgetResources) {

        private val creator = { view: View ->
            AppViewHolder(widgetResources, view)
        }

        fun createWith(parent: ViewGroup) = creator(
            parent.inflate(R.layout.app_widget_list_item)
        )
    }
}
