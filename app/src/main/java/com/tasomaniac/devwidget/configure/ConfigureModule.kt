package com.tasomaniac.devwidget.configure

import android.appwidget.AppWidgetManager
import dagger.Module
import dagger.Provides

typealias ConfigurePinning = Boolean

@Module
object ConfigureModule {

  @Provides
  @JvmStatic
  fun appWidgetId(activity: ConfigureActivity): Int {
    val appWidgetId = activity.intent
        .getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      throw IllegalArgumentException("AppWidgetManager.EXTRA_APPWIDGET_ID is required.")
    }
    return appWidgetId
  }

  @Provides
  @JvmStatic
  fun configurePinning(activity: ConfigureActivity) = activity.configurePin

}
