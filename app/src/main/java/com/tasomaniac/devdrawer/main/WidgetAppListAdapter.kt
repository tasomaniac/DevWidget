package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.widget.WidgetData

class WidgetAppListAdapter(private val data: List<WidgetData>) : RecyclerView.Adapter<AppViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.app_widget_list_item, parent, false)
    return AppViewHolder(view)
  }

  override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun getItemCount() = data.size
}
