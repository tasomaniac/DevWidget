package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.widget.WidgetData
import com.tasomaniac.devdrawer.widget.WidgetNameResolver
import javax.inject.Inject
import kotlin.properties.Delegates.observable

class WidgetListAdapter @Inject constructor(
    private val widgetNameResolver: WidgetNameResolver
) : RecyclerView.Adapter<WidgetViewHolder>() {

  var data: List<Pair<Widget, List<WidgetData>>> by observable(emptyList(), { _, _, _ ->
    notifyDataSetChanged()
  })

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.main_item_widget, parent, false)
    return WidgetViewHolder(widgetNameResolver, view)
  }

  override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
    val (widget, widgetData) = data[position]
    holder.bind(widget, widgetData)
  }

  override fun getItemCount() = data.size
}
