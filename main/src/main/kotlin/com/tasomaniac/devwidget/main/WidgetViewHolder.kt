package com.tasomaniac.devwidget.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.extensions.inflate
import com.tasomaniac.devwidget.widget.preview.WidgetListData
import com.tasomaniac.devwidget.widget.preview.WidgetView

internal class WidgetViewHolder(
    private val widgetView: WidgetView,
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    fun bind(data: WidgetListData, clickAction: (appWidgetId: Int) -> Unit) {
        widgetView.bind(data)
        itemView.setOnClickListener {
            clickAction(data.widget.appWidgetId)
        }
    }

    companion object {
        fun create(parent: ViewGroup, widgetViewFactory: WidgetView.Factory): WidgetViewHolder {
            val view = parent.inflate(R.layout.main_widget_wrapper)
            return WidgetViewHolder(widgetViewFactory.createWith(view), view)
        }
    }
}
