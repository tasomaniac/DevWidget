package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devdrawer.BuildConfig
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.settings.SortingPreferences
import com.tasomaniac.devdrawer.settings.SortingPreferences.Sorting.*
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetViewsService : RemoteViewsService() {

  @Inject lateinit var dao: AppDao
  @Inject lateinit var widgetDataResolver: WidgetDataResolver
  @Inject lateinit var sortingPreferences: SortingPreferences

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onGetViewFactory(intent: Intent): WidgetViewsFactory {
    return WidgetViewsFactory(this, dao, widgetDataResolver, sortingPreferences, intent.appWidgetId)
  }

  class WidgetViewsFactory(
      private val context: Context,
      private val appDao: AppDao,
      private val widgetDataResolver: WidgetDataResolver,
      private val sortingPreferences: SortingPreferences,
      private val appWidgetId: Int
  ) : RemoteViewsService.RemoteViewsFactory {

    private var apps: List<WidgetData> = emptyList()

    override fun onDataSetChanged() {
      val packageNames = appDao.findAppsByWidgetIdSync(appWidgetId)

      apps = packageNames.mapNotNull {
        widgetDataResolver.resolve(it)
      }.sort()
    }

    private fun List<WidgetData>.sort() = when (sortingPreferences.sorting) {
      ORDER_ADDED -> asReversed()
      ALPHABETICALLY_PACKAGES -> sortedBy { it.packageName }
      ALPHABETICALLY_NAMES -> sortedBy { it.label }
    }

    override fun getViewAt(position: Int): RemoteViews {
      val app = apps[position]
      return createViewWith(app)
    }

    private fun createViewWith(app: WidgetData) =
        RemoteViews(BuildConfig.APPLICATION_ID, R.layout.app_widget_list_item).apply {
          setTextViewText(R.id.appWidgetPackageName, app.packageName)
          setTextViewText(R.id.appWidgetLabel, app.label)
          setImageViewBitmap(R.id.appWidgetIcon, app.icon)

          setOnClickFillInIntent(R.id.appWidgetContainer,
              ClickHandlingActivity.createForLaunchApp(app.packageName)
          )
          val uninstall = context.getString(R.string.widget_content_description_uninstall_app, app.label)
          setContentDescription(R.id.appWidgetUninstall, uninstall)
          setOnClickFillInIntent(R.id.appWidgetUninstall,
              ClickHandlingActivity.createForUninstallApp(app.packageName)
          )
          val appDetails = context.getString(R.string.widget_content_description_app_details, app.label)
          setContentDescription(R.id.appWidgetDetails, appDetails)
          setOnClickFillInIntent(R.id.appWidgetDetails,
              ClickHandlingActivity.createForAppDetails(app.packageName)
          )
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
