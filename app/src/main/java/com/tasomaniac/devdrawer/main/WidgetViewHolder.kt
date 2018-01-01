package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.widget.WidgetData
import com.tasomaniac.devdrawer.widget.WidgetNameResolver
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_item_widget.*

class WidgetViewHolder(
    private val widgetNameResolver: WidgetNameResolver,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

  fun bind(widget: Widget, widgetData: List<WidgetData>) {
    mainWidgetTitle.text = widgetNameResolver.resolve(widget)
    mainWidgetAppList.adapter = WidgetAppListAdapter(widgetData)
  }
}

