package com.tasomaniac.devdrawer.main

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tasomaniac.devdrawer.configure.ConfigureActivity
import com.tasomaniac.devdrawer.widget.WidgetNameResolver
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_item_widget.*

class WidgetViewHolder(
    private val widgetNameResolver: WidgetNameResolver,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

  fun bind(data: WidgetListData) {
    mainWidgetTitle.text = widgetNameResolver.resolve(data.widget)
    mainWidgetAppList.adapter = WidgetAppListAdapter(data.widgetData)
    mainWidgetAppList.isNestedScrollingEnabled = false

    itemView.setOnClickListener {
      val intent = Intent(itemView.context, ConfigureActivity::class.java)
          .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, data.widget.appWidgetId)
      itemView.context.startActivity(intent)
    }
  }                                                       
}

