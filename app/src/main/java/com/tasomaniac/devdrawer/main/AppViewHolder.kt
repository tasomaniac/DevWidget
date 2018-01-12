package com.tasomaniac.devdrawer.main

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.extensions.inflate
import com.tasomaniac.devdrawer.widget.WidgetData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.app_widget_list_item.*

class AppViewHolder(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), LayoutContainer {

  fun bind(widgetData: WidgetData) {
    appWidgetIcon.setImageBitmap(widgetData.icon)
    appWidgetLabel.text = widgetData.label
    appWidgetPackageName.text = widgetData.packageName
  }

  companion object {
    fun create(parent: ViewGroup): AppViewHolder {
      val view = parent.inflate(R.layout.app_widget_list_item)
      return AppViewHolder(view)
    }
  }
}
