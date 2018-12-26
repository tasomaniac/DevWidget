package com.tasomaniac.devwidget.widget

import android.app.Application
import android.content.res.Resources
import android.view.View
import android.widget.RemoteViews
import androidx.core.graphics.drawable.toBitmap
import com.tasomaniac.devwidget.data.Action
import com.tasomaniac.devwidget.widget.click.ClickHandlingActivity
import javax.inject.Inject

internal class ItemRemoteViewsCreator @Inject constructor(
    private val application: Application,
    private val resources: Resources,
    private val widgetResources: WidgetResources
) {

    fun createViewWith(app: DisplayApplicationInfo, favAction: Action, widgetWidth: Int) =
        RemoteViews(application.packageName, R.layout.app_widget_list_item).apply {
            setImageViewBitmap(R.id.appWidgetIcon, app.icon.toBitmap())
            val iconSize = widgetResources.resolveAppIconSize(widgetWidth)
            setInt(R.id.appWidgetIcon, "setMaxHeight", iconSize)
            setInt(R.id.appWidgetIcon, "setMaxWidth", iconSize)
            setTextViewText(R.id.appWidgetPackageName, app.packageName)
            setTextColor(R.id.appWidgetPackageName, widgetResources.foregroundColor)
            setTextViewText(R.id.appWidgetLabel, app.label)
            setTextColor(R.id.appWidgetLabel, widgetResources.foregroundColor)

            setOnClickFillInIntent(
                R.id.appWidgetContainer,
                ClickHandlingActivity.createForLaunchApp(app)
            )

            setupMoreActionsButton(app)

            if (widgetResources.shouldDisplayFavAction(widgetWidth)) {
                setupFavActionButton(app, favAction)
                setViewVisibility(R.id.appWidgetFavAction, View.VISIBLE)
            } else {
                setViewVisibility(R.id.appWidgetFavAction, View.GONE)
            }
        }

    private fun RemoteViews.setupFavActionButton(app: DisplayApplicationInfo, favAction: Action) {
        setContentDescription(
            R.id.appWidgetFavAction,
            resources.getString(R.string.widget_content_description_uninstall_app, app.label)
        )
        setImageViewResource(R.id.appWidgetFavAction, widgetResources.resolveFavIcon(favAction))
        setOnClickFillInIntent(
            R.id.appWidgetFavAction,
            ClickHandlingActivity.createForUninstallApp(app)
        )
    }

    private fun RemoteViews.setupMoreActionsButton(app: DisplayApplicationInfo) {
        setContentDescription(
            R.id.appWidgetDetails,
            resources.getString(R.string.widget_content_description_more_options)
        )
        setImageViewResource(R.id.appWidgetDetails, widgetResources.moreActionsIcon)
        setOnClickFillInIntent(
            R.id.appWidgetDetails,
            ClickHandlingActivity.createForActionsDialog(app)
        )
    }
}
