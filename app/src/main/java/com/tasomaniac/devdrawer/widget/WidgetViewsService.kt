package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devdrawer.BuildConfig
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.settings.Sorting.*
import com.tasomaniac.devdrawer.settings.SortingPreferences
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetViewsService : RemoteViewsService() {

  @Inject lateinit var appDao: AppDao
  @Inject lateinit var widgetDataResolver: WidgetDataResolver
  @Inject lateinit var sortingPreferences: SortingPreferences
  @Inject lateinit var widgetResources: WidgetResources

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onGetViewFactory(intent: Intent) = WidgetViewsFactory(this, intent.appWidgetId)

  inner class WidgetViewsFactory(
      private val context: Context,
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
          setImageViewBitmap(R.id.appWidgetIcon, app.icon)
          setTextViewText(R.id.appWidgetPackageName, app.packageName)
          setTextColor(R.id.appWidgetPackageName, widgetResources.foregroundColor)
          setTextViewText(R.id.appWidgetLabel, app.label)
          setTextColor(R.id.appWidgetLabel, widgetResources.foregroundColor)

          setOnClickFillInIntent(R.id.appWidgetContainer,
              ClickHandlingActivity.createForLaunchApp(app.packageName)
          )
          val uninstall = context.getString(R.string.widget_content_description_uninstall_app, app.label)
          setContentDescription(R.id.appWidgetUninstall, uninstall)
          setImageViewResource(R.id.appWidgetUninstall, widgetResources.deleteIcon)
          setOnClickFillInIntent(R.id.appWidgetUninstall,
              ClickHandlingActivity.createForUninstallApp(app.packageName)
          )
          val appDetails = context.getString(R.string.widget_content_description_app_details, app.label)
          setContentDescription(R.id.appWidgetDetails, appDetails)
          setImageViewResource(R.id.appWidgetDetails, widgetResources.settingsIcon)
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
