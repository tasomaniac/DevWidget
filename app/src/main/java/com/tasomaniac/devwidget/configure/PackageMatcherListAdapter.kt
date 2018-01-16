package com.tasomaniac.devwidget.configure

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import javax.inject.Inject
import kotlin.properties.Delegates.observable

class PackageMatcherListAdapter @Inject constructor(
    private val viewHolderFactory: PackageMatcherViewHolder.Factory
) : RecyclerView.Adapter<PackageMatcherViewHolder>() {

  var data by observable(emptyList<String>()) { _, _, _ ->
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewHolderFactory.createWith(parent)

  override fun onBindViewHolder(holder: PackageMatcherViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun onViewRecycled(holder: PackageMatcherViewHolder) {
    holder.unbind()
  }

  override fun getItemCount() = data.size
}
