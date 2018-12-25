package com.tasomaniac.devwidget.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.devwidget.configure.WidgetConfigureCommand
import com.tasomaniac.devwidget.navigation.Navigator
import com.tasomaniac.devwidget.widget.preview.WidgetListData
import com.tasomaniac.devwidget.widget.preview.WidgetViewHolder
import javax.inject.Inject

internal class WidgetListAdapter @Inject constructor(
    private val navigator: Navigator,
    private val widgetViewHolderFactory: WidgetViewHolder.Factory
) : RecyclerView.Adapter<WidgetViewHolder>() {

    var data = emptyList<WidgetListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        widgetViewHolderFactory.createWith(parent)

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        holder.bind(data[position], clickAction = { appWidgetId ->
            navigator.navigate(WidgetConfigureCommand(appWidgetId))
        })
    }

    override fun getItemCount() = data.size
}
