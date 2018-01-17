package com.tasomaniac.devwidget.main

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import javax.inject.Inject

class WidgetListAdapter @Inject constructor(
    private val widgetViewHolderFactory: WidgetViewHolder.Factory
) : RecyclerView.Adapter<WidgetViewHolder>() {

    var data = emptyList<WidgetListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        widgetViewHolderFactory.createWith(parent)

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}
