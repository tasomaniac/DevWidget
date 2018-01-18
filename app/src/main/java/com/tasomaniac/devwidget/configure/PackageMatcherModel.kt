package com.tasomaniac.devwidget.configure

import android.arch.lifecycle.ViewModel
import com.jakewharton.rx.replayingShare
import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.data.FilterDao
import com.tasomaniac.devwidget.data.insertApps
import com.tasomaniac.devwidget.data.insertPackageMatchers
import com.tasomaniac.devwidget.rx.flatten
import com.tasomaniac.devwidget.widget.matchPackage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PackageMatcherModel @Inject constructor(
    private val packageResolver: PackageResolver,
    private val appDao: AppDao,
    private val filterDao: FilterDao,
    val appWidgetId: Int
) : ViewModel() {

    @CheckReturnValue
    fun insertPackageMatcher(packageMatcher: String): Completable {
        return filterDao.insertPackageMatchers(appWidgetId, listOf(packageMatcher))
    }

    @CheckReturnValue
    fun findAndInsertMatchingApps(): Completable {
        return filterDao.findFiltersByWidgetId(appWidgetId)
            .firstOrError()
            .flatten()
            .flatMapCompletable { packageMatcher ->
                insertMatchingApps(packageMatcher)
            }
    }

    private fun insertMatchingApps(packageMatcher: String): Completable {
        return Observable
            .fromIterable(packageResolver.allLauncherPackages())
            .filter(matchPackage(packageMatcher))
            .toList()
            .flatMapCompletable {
                appDao.insertApps(appWidgetId, packageMatcher, it)
            }
    }

    @CheckReturnValue
    fun findPossiblePackageMatchers(): Flowable<List<String>> {
        return Flowable.combineLatest(
            Flowable.fromCallable { packageResolver.allLauncherPackages().toPackageMatchers() },
            packageMatchers(),
            BiFunction { possible, available ->
                possible - available
            })
    }

    @CheckReturnValue
    fun packageMatchers() = filterDao.findFiltersByWidgetId(appWidgetId).replayingShare()
}

