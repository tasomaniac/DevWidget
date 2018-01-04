package com.tasomaniac.devdrawer.configure

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlin.properties.Delegates.observable

class PackageMatcherListAdapter : RecyclerView.Adapter<PackageMatcherViewHolder>() {

  var data by observable(emptyList<String>()) { _, _, _ ->
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageMatcherViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(android.R.layout.simple_list_item_1, parent, false)
    return PackageMatcherViewHolder(view as TextView)
  }

  override fun onBindViewHolder(holder: PackageMatcherViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun getItemCount() = data.size
}
