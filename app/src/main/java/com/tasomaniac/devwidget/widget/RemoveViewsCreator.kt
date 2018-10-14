package com.tasomaniac.devwidget.widget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.IdRes
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.configure.ConfigureActivity
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.settings.OpacityPreferences
import com.tasomaniac.devwidget.widget.click.ClickHandlingActivity
import com.tasomaniac.devwidget.widget.click.WidgetRefreshActivity
import javax.inject.Inject

class RemoveViewsCreator(
    private val app: Application,
    private val widgetResources: WidgetResources,
    private val opacityPreferences: OpacityPreferences,
    private val widget: Widget,
    private val minWidth: Int
) {

    fun create() = RemoteViews(app.packageName, R.layout.app_widget).apply {
        setupBackgroundShade()
        setupHeader()
        setRemoteAdapter(R.id.widgetAppList, remoteAdapter(app))
        setupClickHandling()
        setupEmptyView()
    }

    private fun RemoteViews.setupHeader() {
        setTextViewText(R.id.widgetTitle, widget.name)
        setTextColor(R.id.widgetTitle, widgetResources.foregroundColor)

        setupConfigureButton(R.id.widgetConfigure)
        setImageViewResource(R.id.widgetConfigure, widgetResources.settingsIcon)
        setupDevOptionsButton()
        setupRefreshButton()
    }

    private fun RemoteViews.setupConfigureButton(@IdRes buttonId: Int) {
        setContentDescription(
            buttonId,
            app.getString(R.string.widget_content_description_configure, widget)
        )
        val intent = ConfigureActivity.createIntent(app, widget.appWidgetId)
            .toPendingActivity(app, widget.appWidgetId)
        setOnClickPendingIntent(buttonId, intent)
    }

    private fun RemoteViews.setupDevOptionsButton() {
        val intent = Intent(ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            .toPendingActivity(app)
        setOnClickPendingIntent(R.id.widgetDevOptions, intent)
        setImageViewResource(R.id.widgetDevOptions, widgetResources.devOptionsIcon)
    }

    private fun RemoteViews.setupRefreshButton() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setViewVisibility(R.id.widgetRefresh, View.GONE)
        } else {
            setViewVisibility(R.id.widgetRefresh, View.VISIBLE)
            val intent = WidgetRefreshActivity.createIntent(app, widget.appWidgetId)
                .toPendingActivity(app, widget.appWidgetId)
            setOnClickPendingIntent(R.id.widgetRefresh, intent)
            setImageViewResource(R.id.widgetRefresh, widgetResources.refreshIcon)
        }
    }

    private fun RemoteViews.setupBackgroundShade() {
        val shadeColor = opacityPreferences.backgroundColor
        setInt(R.id.shade, "setBackgroundColor", shadeColor)
        setViewVisibility(R.id.shade, if (shadeColor == 0) View.GONE else View.VISIBLE)
    }

    private fun RemoteViews.setupEmptyView() {
        setEmptyView(R.id.widgetAppList, R.id.widgetEmpty)
        setTextColor(R.id.widgetEmpty, widgetResources.foregroundColor)
        setTextViewCompoundDrawablesRelative(
            R.id.widgetEmpty,
            0,
            0,
            widgetResources.settingsIcon,
            0
        )
        setupConfigureButton(R.id.widgetEmpty)
    }

    private fun RemoteViews.setupClickHandling() {
        val intentTemplate = ClickHandlingActivity.intent(app)
            .toPendingActivity(app, widget.appWidgetId)
        setPendingIntentTemplate(R.id.widgetAppList, intentTemplate)
    }

    private fun remoteAdapter(context: Context): Intent {
        return Intent(context, WidgetViewsService::class.java).apply {
            putExtra(WidgetViewsService.WIDGET_WIDTH, minWidth)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widget.appWidgetId)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }
    }

    class Factory @Inject constructor(
        private val app: Application,
        private val widgetResources: WidgetResources,
        private val opacityPreferences: OpacityPreferences
    ) {

        fun create(widget: Widget, minWidth: Int) =
            RemoveViewsCreator(app, widgetResources, opacityPreferences, widget, minWidth)
    }
}
