package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue

interface WidgetUpdater {

    val appWidgetManager: AppWidgetManager

    @CheckReturnValue
    fun update(
        appWidgetId: Int,
        name: String,
        widgetOptions: Bundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
    ): Completable

    @CheckReturnValue
    fun updateAll(): Completable

    fun notifyWidgetDataChanged(appWidgetId: Int)

    fun hasWidgets(): Boolean
}
