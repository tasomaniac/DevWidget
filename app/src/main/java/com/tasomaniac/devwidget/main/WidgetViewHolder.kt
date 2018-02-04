package com.tasomaniac.devwidget.main

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.configure.ConfigureActivity
import com.tasomaniac.devwidget.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_item_widget.*
import javax.inject.Inject

class WidgetViewHolder(
    private val widgetNameResolver: WidgetNameResolver,
    private val appViewHolderFactory: AppViewHolder.Factory,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(data: WidgetListData) {
        mainWidgetTitle.text = widgetNameResolver.resolve(data.widget)
        mainWidgetAppList.adapter = WidgetAppListAdapter(appViewHolderFactory, data.apps)

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, ConfigureActivity::class.java)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, data.widget.appWidgetId)
            itemView.context.startActivity(intent)
        }
    }

    class Factory @Inject constructor(
        widgetNameResolver: WidgetNameResolver,
        appViewHolderFactory: AppViewHolder.Factory
    ) {

        private val creator = { view: View ->
            WidgetViewHolder(widgetNameResolver, appViewHolderFactory, view)
        }

        fun createWith(parent: ViewGroup) = creator(
            parent.inflate(R.layout.main_item_widget)
        )
    }
}

