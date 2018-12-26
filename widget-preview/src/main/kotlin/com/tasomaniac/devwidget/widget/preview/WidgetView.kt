package com.tasomaniac.devwidget.widget.preview

import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.widget_preview_layout.mainWidgetAppList
import kotlinx.android.synthetic.main.widget_preview_layout.mainWidgetTitle
import javax.inject.Inject

class WidgetView internal constructor(
    private val widgetNameResolver: WidgetNameResolver,
    private val appViewHolderFactory: AppViewHolder.Factory,
    override val containerView: View
) : LayoutContainer {

    fun bind(data: WidgetListData) {
        mainWidgetTitle.text = widgetNameResolver.resolve(data.widget)
        mainWidgetAppList.adapter = AppListAdapter(appViewHolderFactory, data.apps, data.favAction)
    }

    class Factory @Inject internal constructor(
        private val widgetNameResolver: WidgetNameResolver,
        private val appViewHolderFactory: AppViewHolder.Factory
    ) {

        fun createWith(view: View) = WidgetView(widgetNameResolver, appViewHolderFactory, view)
    }
}
