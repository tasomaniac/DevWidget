package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.widget.WidgetData

class WidgetListAdapter(private val data: List<Pair<Widget, List<WidgetData>>>) : RecyclerView.Adapter<WidgetViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.main_item_widget, parent, false)
    return WidgetViewHolder(view)
  }

  override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
    val (widget, widgetData) = data[position]
    holder.bind(widget, widgetData)
  }

  override fun getItemCount() = data.size
}
