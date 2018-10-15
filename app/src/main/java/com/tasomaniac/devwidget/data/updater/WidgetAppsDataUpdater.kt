package com.tasomaniac.devwidget.data.updater

import com.tasomaniac.devwidget.data.App
import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.data.FilterDao
import com.tasomaniac.devwidget.extensions.flatten
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class WidgetAppsDataUpdater @Inject constructor(
    private val filterDao: FilterDao,
    private val appDao: AppDao,
    private val packageResolver: PackageResolver
) {

    @CheckReturnValue
    fun findAndInsertMatchingApps(appWidgetId: Int): Completable {
        return filterDao.findFiltersByWidgetId(appWidgetId)
            .firstOrError()
            .flatten()
            .flatMapCompletable { packageMatcher ->
                insertMatchingApps(appWidgetId, packageMatcher)
            }
    }

    private fun insertMatchingApps(appWidgetId: Int, packageMatcher: String): Completable {
        return Observable
            .fromIterable(packageResolver.allApplications())
            .filter(matchPackage(packageMatcher))
            .toList()
            .flatMapCompletable { packageNames ->
                val apps = packageNames.map {
                    App(it, packageMatcher, appWidgetId)
                }
                appDao.insertApps(apps)
            }
    }
}
