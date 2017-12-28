package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devdrawer.BuildConfig
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.App
import com.tasomaniac.devdrawer.data.AppDao
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetViewsService : RemoteViewsService() {

  @Inject lateinit var appDao: AppDao

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onGetViewFactory(intent: Intent): WidgetViewsFactory {
    return WidgetViewsFactory(appDao, intent.appWidgetId)
  }

  class WidgetViewsFactory(
      private val appDao: AppDao,
      private val appWidgetId: Int
  ) : RemoteViewsService.RemoteViewsFactory {

    private var apps: List<App> = emptyList()

    override fun onDataSetChanged() {
      apps = appDao.findAppsByWidgetId(appWidgetId)
    }

    override fun getViewAt(position: Int): RemoteViews {
      val app = apps[position]
      return RemoteViews(BuildConfig.APPLICATION_ID, R.layout.app_widget_list_item).apply {
        setTextViewText(R.id.appWidgetItemPackageName, app.packageName)
      }
    }

    override fun getCount() = apps.size

    override fun getViewTypeCount() = 1
    override fun getItemId(position: Int) = apps[position].packageName.hashCode().toLong()
    override fun hasStableIds() = true
    override fun getLoadingView(): RemoteViews? = null
    override fun onCreate() = Unit
    override fun onDestroy() = Unit

  }

  companion object {
    private val Intent.appWidgetId
      get() = getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
  }
}
