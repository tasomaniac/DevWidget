package com.tasomaniac.devwidget.widget.preview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.widget_preview_layout.mainWidgetAppList
import kotlinx.android.synthetic.main.widget_preview_layout.mainWidgetTitle
import javax.inject.Inject

class WidgetViewHolder internal constructor(
    private val widgetNameResolver: WidgetNameResolver,
    private val appViewHolderFactory: AppViewHolder.Factory,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(data: WidgetListData, clickAction: (appWidgetId: Int) -> Unit) {
        mainWidgetTitle.text = widgetNameResolver.resolve(data.widget)
        mainWidgetAppList.adapter = AppListAdapter(appViewHolderFactory, data.apps)

        itemView.setOnClickListener {
            clickAction(data.widget.appWidgetId)
        }
    }

    class Factory @Inject internal constructor(
        widgetNameResolver: WidgetNameResolver,
        appViewHolderFactory: AppViewHolder.Factory
    ) {

        private val creator = { view: View ->
            WidgetViewHolder(
                widgetNameResolver,
                appViewHolderFactory,
                view
            )
        }

        fun createWith(parent: ViewGroup) = creator(
            parent.inflate(R.layout.widget_preview_layout)
        )
    }
}
