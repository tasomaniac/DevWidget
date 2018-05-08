package com.tasomaniac.devwidget.widget

import android.content.res.Resources
import android.widget.RemoteViews
import com.tasomaniac.devwidget.BuildConfig
import com.tasomaniac.devwidget.R
import javax.inject.Inject

class ItemRemoteViewsCreator @Inject constructor(
    private val resources: Resources,
    private val widgetResources: WidgetResources
) {

    fun createViewWith(app: DisplayApplicationInfo, widgetWidth: Int) =
        RemoteViews(BuildConfig.APPLICATION_ID, R.layout.app_widget_list_item).apply {
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
            val uninstall =
                resources.getString(R.string.widget_content_description_uninstall_app, app.label)
            setContentDescription(R.id.appWidgetUninstall, uninstall)
            setImageViewResource(R.id.appWidgetUninstall, widgetResources.deleteIcon)
            setOnClickFillInIntent(
                R.id.appWidgetUninstall,
                ClickHandlingActivity.createForUninstallApp(app)
            )
            val appDetails =
                resources.getString(R.string.widget_content_description_app_details, app.label)
            setContentDescription(R.id.appWidgetDetails, appDetails)
            setImageViewResource(R.id.appWidgetDetails, widgetResources.settingsIcon)
            setOnClickFillInIntent(
                R.id.appWidgetDetails,
                ClickHandlingActivity.createForAppDetails(app)
            )
        }
}
