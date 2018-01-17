package com.tasomaniac.devwidget.main

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

class WidgetDiffCallbacks(
    private val oldItems: List<WidgetListData>,
    private val newItems: List<WidgetListData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (oldWidget, _) = oldItems[oldPosition]
        val (newWidget, _) = newItems[newPosition]
        return oldWidget.appWidgetId == newWidget.appWidgetId
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
        oldItems[oldPosition] == newItems[newPosition]

    fun calculateDiffAndDispatchUpdates(adapter: RecyclerView.Adapter<*>) {
        DiffUtil.calculateDiff(this).dispatchUpdatesTo(adapter)
    }

    companion object {
        val EMPTY = WidgetDiffCallbacks(emptyList(), emptyList())
    }
}
