package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_NAMES
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_PACKAGES
import com.tasomaniac.devwidget.settings.Sorting.ORDER_ADDED
import com.tasomaniac.devwidget.settings.SortingPreferences
import dagger.android.AndroidInjection
import javax.inject.Inject

class WidgetViewsService : RemoteViewsService() {

    @Inject lateinit var appDao: AppDao
    @Inject lateinit var widgetDataResolver: WidgetDataResolver
    @Inject lateinit var sortingPreferences: SortingPreferences
    @Inject lateinit var itemCreator: ItemRemoveViewsCreator

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent) = WidgetViewsFactory(intent.appWidgetId)

    inner class WidgetViewsFactory(
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
            return itemCreator.createViewWith(app)
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
            get() = getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
    }

}
