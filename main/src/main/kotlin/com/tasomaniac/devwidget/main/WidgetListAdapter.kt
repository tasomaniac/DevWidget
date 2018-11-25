package com.tasomaniac.devwidget.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

internal class WidgetListAdapter @Inject constructor(
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
