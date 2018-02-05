package com.tasomaniac.devwidget.widget.chooser

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tasomaniac.devwidget.widget.DisplayResolveInfo
import javax.inject.Inject

class ResolveListAdapter @Inject constructor(
    private val viewHolderFactory: ActivityChooserViewHolder.Factory
) : RecyclerView.Adapter<ActivityChooserViewHolder>() {

    private var mList: List<DisplayResolveInfo> = emptyList()

    var displayExtendedInfo = false
    var selectionEnabled = false
    var checkedItemPosition = RecyclerView.NO_POSITION
        private set

    var itemClickListener: ItemClickListener? = null
    var itemLongClickListener: ItemLongClickListener? = null

    override fun getItemCount() = mList.size
    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        viewHolderFactory.createWith(parent)

    override fun onBindViewHolder(
        holder: ActivityChooserViewHolder,
        position: Int,
        payloads: List<Any>?
    ) {
        super.onBindViewHolder(holder, position, payloads)

        val checked = position == checkedItemPosition
        holder.itemView.isActivated = checked
    }

    override fun onBindViewHolder(holder: ActivityChooserViewHolder, position: Int) {
        val info = mList[position]

        holder.bind(info)

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(info)
            setItemChecked(holder.adapterPosition)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.onItemLongClick(info) ?: false
        }
    }

    fun setItemChecked(position: Int) {
        if (!selectionEnabled) {
            return
        }

        notifyItemChanged(position, true)
        notifyItemChanged(checkedItemPosition, false)

        checkedItemPosition = position
    }

    fun setApplications(list: List<DisplayResolveInfo>) {
        this.mList = list
        notifyDataSetChanged()
    }

}
