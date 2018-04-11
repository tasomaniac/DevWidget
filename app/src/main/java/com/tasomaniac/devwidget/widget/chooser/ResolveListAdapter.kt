package com.tasomaniac.devwidget.widget.chooser

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import javax.inject.Inject

class ResolveListAdapter @Inject constructor(
    private val viewHolderFactory: ActivityChooserViewHolder.Factory
) : ListAdapter<DisplayResolveInfo, ActivityChooserViewHolder>(DiffUtilsCallback) {

    var itemClickListener: (DisplayResolveInfo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        viewHolderFactory.createWith(parent)

    override fun onBindViewHolder(holder: ActivityChooserViewHolder, position: Int) =
        holder.bind(getItem(position), itemClickListener)
}

object DiffUtilsCallback : DiffUtil.ItemCallback<DisplayResolveInfo>() {
    override fun areItemsTheSame(oldItem: DisplayResolveInfo, newItem: DisplayResolveInfo) = oldItem == newItem

    override fun areContentsTheSame(oldItem: DisplayResolveInfo, newItem: DisplayResolveInfo) = true
}
