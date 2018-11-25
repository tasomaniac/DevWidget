package com.tasomaniac.devwidget.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.configure.WidgetConfigureCommand
import com.tasomaniac.devwidget.extensions.inflate
import com.tasomaniac.devwidget.navigation.Navigator
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.main_item_widget.mainWidgetAppList
import kotlinx.android.synthetic.main.main_item_widget.mainWidgetTitle
import javax.inject.Inject

internal class WidgetViewHolder(
    private val navigator: Navigator,
    private val widgetNameResolver: WidgetNameResolver,
    private val appViewHolderFactory: AppViewHolder.Factory,
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(data: WidgetListData) {
        mainWidgetTitle.text = widgetNameResolver.resolve(data.widget)
        mainWidgetAppList.adapter = AppListAdapter(appViewHolderFactory, data.apps)

        itemView.setOnClickListener {
            navigator.navigate(WidgetConfigureCommand(data.widget.appWidgetId))
        }
    }

    class Factory @Inject constructor(
        navigator: Navigator,
        widgetNameResolver: WidgetNameResolver,
        appViewHolderFactory: AppViewHolder.Factory
    ) {

        private val creator = { view: View ->
            WidgetViewHolder(navigator, widgetNameResolver, appViewHolderFactory, view)
        }

        fun createWith(parent: ViewGroup) = creator(
            parent.inflate(R.layout.main_item_widget)
        )
    }
}
