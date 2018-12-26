package com.tasomaniac.devwidget.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.navigation.Navigator
import com.tasomaniac.devwidget.navigation.widgetConfigureCommand
import com.tasomaniac.devwidget.widget.preview.WidgetListData
import com.tasomaniac.devwidget.widget.preview.WidgetView
import javax.inject.Inject

internal class WidgetListAdapter @Inject constructor(
    private val navigator: Navigator,
    private val widgetViewFactory: WidgetView.Factory
) : RecyclerView.Adapter<WidgetViewHolder>() {

    var data = emptyList<WidgetListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WidgetViewHolder.create(parent, widgetViewFactory)

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(data[position], clickAction = { appWidgetId ->
            navigator.navigate(widgetConfigureCommand(appWidgetId))
        })
    }

    override fun getItemCount() = data.size
}
