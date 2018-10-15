package com.tasomaniac.devwidget.configure

import androidx.lifecycle.ViewModel
import com.jakewharton.rx.ReplayingShare
import com.tasomaniac.devwidget.data.Filter
import com.tasomaniac.devwidget.data.FilterDao
import com.tasomaniac.devwidget.data.updater.PackageResolver
import com.tasomaniac.devwidget.data.updater.WidgetAppsDataUpdater
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.rxkotlin.combineLatest
import javax.inject.Inject

class PackageMatcherModel @Inject constructor(
    private val packageResolver: PackageResolver,
    private val filterDao: FilterDao,
    private val widgetAppsDataUpdater: WidgetAppsDataUpdater,
    val appWidgetId: Int
) : ViewModel() {

    @CheckReturnValue
    fun insertPackageMatcher(packageMatcher: String): Completable {
        return filterDao.insertFilter(Filter(packageMatcher, appWidgetId))
    }

    @CheckReturnValue
    fun findAndInsertMatchingApps() = widgetAppsDataUpdater.findAndInsertMatchingApps(appWidgetId)

    @CheckReturnValue
    fun findPossiblePackageMatchers(): Flowable<List<String>> {
        return Flowable.fromCallable { packageResolver.allApplications().toPackageMatchers() }
            .combineLatest(findAvailablePackageMatchers())
            .map { (possible, available) ->
                possible - available
            }
    }

    @CheckReturnValue
    fun findAvailablePackageMatchers(): Flowable<List<String>> =
        filterDao.findFiltersByWidgetId(appWidgetId).compose(ReplayingShare.instance())
}
