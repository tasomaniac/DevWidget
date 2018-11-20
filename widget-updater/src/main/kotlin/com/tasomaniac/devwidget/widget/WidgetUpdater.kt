package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import com.tasomaniac.devwidget.data.Widget
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue

interface WidgetUpdater {

    val appWidgetManager: AppWidgetManager

    @CheckReturnValue
    fun update(widget: Widget, widgetOptions: Bundle = widget.defaultOptions()): Completable

    @CheckReturnValue
    fun updateAll(): Completable

    fun notifyWidgetDataChanged(appWidgetId: Int)

    private fun Widget.defaultOptions(): Bundle {
        return appWidgetManager.getAppWidgetOptions(appWidgetId)
    }
}
