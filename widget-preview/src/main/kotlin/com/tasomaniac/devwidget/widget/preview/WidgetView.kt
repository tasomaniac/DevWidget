package com.tasomaniac.devwidget.widget.preview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tasomaniac.devwidget.widget.WidgetResources
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.widget_preview_layout.widgetPreviewAppList
import javax.inject.Inject

class WidgetView private constructor(
    private val appViewHolderFactory: AppViewHolder.Factory,
    private val widgetResources: WidgetResources,
    override val containerView: View
) : LayoutContainer {

    private val widgetTitle = containerView.findViewById<TextView>(R.id.widgetTitle)
    private val widgetDevOptions = containerView.findViewById<ImageView>(R.id.widgetDevOptions)
    private val widgetConfigure = containerView.findViewById<ImageView>(R.id.widgetConfigure)

    fun bind(data: WidgetListData) {
        widgetTitle.text = data.widget.name
        widgetTitle.setTextColor(widgetResources.foregroundColor)
        widgetDevOptions.setImageResource(widgetResources.devOptionsIcon)
        widgetConfigure.setImageResource(widgetResources.settingsIcon)
        widgetPreviewAppList.adapter = AppListAdapter(appViewHolderFactory, data.apps, data.favAction)
    }

    class Factory @Inject internal constructor(
        private val appViewHolderFactory: AppViewHolder.Factory,
        private val widgetResources: WidgetResources
    ) {

        fun createWith(view: View) = WidgetView(appViewHolderFactory, widgetResources, view)
    }
}
