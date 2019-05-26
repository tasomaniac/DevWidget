package com.tasomaniac.devwidget.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.tasomaniac.devwidget.data.Action
import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.data.FavActionDao
import com.tasomaniac.devwidget.data.findFavActionByWidgetIdSync
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_NAMES
import com.tasomaniac.devwidget.settings.Sorting.ALPHABETICALLY_PACKAGES
import com.tasomaniac.devwidget.settings.Sorting.ORDER_ADDED
import com.tasomaniac.devwidget.settings.SortingPreferences
import dagger.android.AndroidInjection
import javax.inject.Inject

internal class WidgetViewsService : RemoteViewsService() {

    @Inject lateinit var appDao: AppDao
    @Inject lateinit var favActionDao: FavActionDao
    @Inject lateinit var applicationInfoResolver: ApplicationInfoResolver
    @Inject lateinit var sortingPreferences: SortingPreferences
    @Inject lateinit var itemCreator: ItemRemoteViewsCreator

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent) = WidgetViewsFactory(intent.appWidgetId, intent.widgetWidth)

    inner class WidgetViewsFactory(
        private val appWidgetId: Int,
        private val widgetWidth: Int
    ) : RemoteViewsFactory {

        private var apps: List<DisplayApplicationInfo> = emptyList()
        private var favAction: Action = Action.UNINSTALL

        override fun onDataSetChanged() {
            val packageNames = appDao.findAppsByWidgetIdSync(appWidgetId)
            apps = packageNames.flatMap(applicationInfoResolver::resolve).sort()
            favAction = favActionDao.findFavActionByWidgetIdSync(appWidgetId)
        }

        private fun List<DisplayApplicationInfo>.sort() = when (sortingPreferences.sorting) {
            ORDER_ADDED -> asReversed()
            ALPHABETICALLY_PACKAGES -> sortedBy { it.packageName }
            ALPHABETICALLY_NAMES -> sortedBy { it.label.toString() }
        }

        override fun getViewAt(position: Int): RemoteViews {
            val app = apps[position]
            return itemCreator.createViewWith(app, favAction, widgetWidth)
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
        const val WIDGET_WIDTH = "widget_width"

        private val Intent.widgetWidth: Int
            get() = getIntExtra(WIDGET_WIDTH, Int.MAX_VALUE)

        private val Intent.appWidgetId
            get() = getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
    }
}
