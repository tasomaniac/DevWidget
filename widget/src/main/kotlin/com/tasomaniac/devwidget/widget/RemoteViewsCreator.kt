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
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import com.tasomaniac.devwidget.configure.WidgetConfigureCommand
import com.tasomaniac.devwidget.data.Widget
import com.tasomaniac.devwidget.extensions.toPendingActivity
import com.tasomaniac.devwidget.settings.Opacity
import com.tasomaniac.devwidget.settings.OpacityPreferences
import com.tasomaniac.devwidget.widget.click.ClickHandlingActivity
import com.tasomaniac.devwidget.widget.click.HeaderOptionsActivity
import com.tasomaniac.devwidget.widget.click.WidgetRefreshActivity
import javax.inject.Inject

internal class RemoteViewsCreator(
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

        if (widgetResources.shouldDisplayExtendedHeader(minWidth)) {
            setupConfigureButton(R.id.widgetConfigure)
            setImageViewResource(R.id.widgetConfigure, widgetResources.settingsIcon)
            setupDevOptionsButton()
            setupRefreshButton()

            setViewVisibility(R.id.widgetDevOptions, View.VISIBLE)
            setViewVisibility(R.id.widgetRefresh, View.VISIBLE)
        } else {
            setupCompactHeader()

            setViewVisibility(R.id.widgetDevOptions, View.GONE)
            setViewVisibility(R.id.widgetRefresh, View.GONE)
        }
    }

    private fun RemoteViews.setupConfigureButton(@IdRes buttonId: Int) {
        setContentDescription(
            buttonId,
            app.getString(R.string.widget_content_description_configure, widget.name)
        )
        val intent = WidgetConfigureCommand(widget.appWidgetId)
            .createIntent(app)
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

    private fun RemoteViews.setupCompactHeader() {
        setContentDescription(R.id.widgetConfigure, app.getString(R.string.widget_content_description_more_options))
        setImageViewResource(R.id.widgetConfigure, widgetResources.moreActionsIcon)

        val intent = HeaderOptionsActivity.createIntent(app, widget.appWidgetId)
            .toPendingActivity(app, widget.appWidgetId)
        setOnClickPendingIntent(R.id.widgetConfigure, intent)
    }

    private fun RemoteViews.setupBackgroundShade() {
        val shadeColor = opacityPreferences.opacity.toBackgroundColor()
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

    @ColorInt @Suppress("MagicNumber")
    private fun Opacity.toBackgroundColor(): Int {
        val backgroundColor = widgetResources.foregroundColorInverse
        val opacity = stringVale(app.resources).toInt()
        return backgroundColor and 0xffffff or (opacity * 255 / 100 shl 24)
    }

    class Factory @Inject constructor(
        private val app: Application,
        private val widgetResources: WidgetResources,
        private val opacityPreferences: OpacityPreferences
    ) {

        fun create(widget: Widget, minWidth: Int) =
            RemoteViewsCreator(app, widgetResources, opacityPreferences, widget, minWidth)
    }
}
