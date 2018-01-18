package com.tasomaniac.devwidget.configure

import com.tasomaniac.devwidget.data.AppDao
import com.tasomaniac.devwidget.data.FilterDao
import com.tasomaniac.devwidget.data.insertApps
import com.tasomaniac.devwidget.data.insertPackageMatchers
import com.tasomaniac.devwidget.rx.flatten
import com.tasomaniac.devwidget.widget.matchPackage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ConfigureUseCase @Inject constructor(
    private val packageResolver: PackageResolver,
    private val appDao: AppDao,
    private val filterDao: FilterDao,
    val appWidgetId: Int
) {

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
    fun findPossiblePackageMatchers(): Observable<List<String>> {
        return Observable.combineLatest(
            Observable.fromCallable { packageResolver.allLauncherPackages().toPackageMatchers() },
            packageMatchers(),
            BiFunction { possible, available ->
                possible - available
            })
    }

    private fun List<String>.toPackageMatchers(): List<String> {
        return flatMap {
            var currentPackage = it

            it.split('.')
                .foldRight(setOf(it)) { part, acc ->
                    currentPackage = currentPackage.removeSuffix(".$part")
                    acc + "$currentPackage.*"
                }
                .reversed()
        }.distinct()
    }

    @CheckReturnValue
    fun packageMatchers() = filterDao.findFiltersByWidgetId(appWidgetId).toObservable()

    fun release() = Unit

}
