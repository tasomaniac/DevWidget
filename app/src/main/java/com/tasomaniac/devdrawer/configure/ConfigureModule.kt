package com.tasomaniac.devdrawer.configure

import android.appwidget.AppWidgetManager
import dagger.Module
import dagger.Provides

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

}
