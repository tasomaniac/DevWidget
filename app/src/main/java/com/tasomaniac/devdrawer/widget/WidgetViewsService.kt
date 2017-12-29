package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devdrawer.BuildConfig
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

class WidgetViewsService : RemoteViewsService() {

  @Inject lateinit var appDao: AppDao

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onGetViewFactory(intent: Intent): WidgetViewsFactory {
    return WidgetViewsFactory(appDao, packageManager, intent.appWidgetId)
  }

  class WidgetViewsFactory(
      private val appDao: AppDao,
      private val packageManager: PackageManager,
      private val appWidgetId: Int
  ) : RemoteViewsService.RemoteViewsFactory {

    private var apps: List<WidgetData> = emptyList()

    override fun onDataSetChanged() {
      val packageNames = appDao.findAppsByWidgetId(appWidgetId)

      apps = packageNames.mapNotNull {
        try {
          val applicationInfo = packageManager.getPackageInfo(it, PackageManager.GET_ACTIVITIES).applicationInfo

          WidgetData(
              label = applicationInfo.loadLabel(packageManager).toString(),
              packageName = applicationInfo.packageName,
              icon = applicationInfo.loadIcon(packageManager)
          )
        } catch (e: PackageManager.NameNotFoundException) {
          Timber.e(e)
          null
        }
      }

    }

    override fun getViewAt(position: Int): RemoteViews {
      val app = apps[position]
      return RemoteViews(BuildConfig.APPLICATION_ID, R.layout.app_widget_list_item).apply {
        setTextViewText(R.id.appWidgetPackageName, app.packageName)
        setTextViewText(R.id.appWidgetLabel, app.label)
        setImageViewBitmap(R.id.appWidgetIcon, (app.icon as BitmapDrawable).bitmap)
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

  data class WidgetData(val label: String, val packageName: String, val icon: Drawable)
}
