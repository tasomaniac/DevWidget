package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.widget.WidgetData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_item_widget.*

class WidgetViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

  fun bind(widget: Widget, widgetData: List<WidgetData>) {
    mainWidgetTitle.text = widget.name
    mainWidgetAppList.adapter = WidgetAppListAdapter(widgetData)
  }
}

