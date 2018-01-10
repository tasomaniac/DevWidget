package com.tasomaniac.devdrawer.configure

import android.content.Intent
import android.content.pm.PackageManager
import android.support.annotation.VisibleForTesting
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.FilterDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.insertApps
import com.tasomaniac.devdrawer.data.insertFilters
import com.tasomaniac.devdrawer.data.insertWidget
import com.tasomaniac.devdrawer.data.updateWidget
import com.tasomaniac.devdrawer.rx.Debouncer
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.rx.flatten
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import com.tasomaniac.devdrawer.widget.matchPackage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConfigureUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val dao: Dao,
    private val filterDao: FilterDao,
    private val widgetUpdater: WidgetUpdater,
    val appWidgetId: Int,
    debouncer: Debouncer<String>,
    scheduling: SchedulingStrategy
) {

  private val disposables = CompositeDisposable()
  private val widgetNameSubject: BehaviorSubject<String> = BehaviorSubject.create()

  init {
    disposables.add(insertIfNotFound()
        .andThen(widgetNameSubject
            .distinctUntilChanged()
            .compose(debouncer)
            .flatMapCompletable(::updateWidget))
        .compose(scheduling.forCompletable())
        .subscribe {
          // no-op
        }
    )
  }

  private fun insertIfNotFound(): Completable {
    return dao.findWidgetById(appWidgetId)
        .isEmpty.filter { it }
        .flatMapCompletable {
          dao.insertWidget(Widget(appWidgetId))
        }
  }

  private fun updateWidget(widgetName: String): Completable? {
    val widget = Widget(appWidgetId, widgetName)
    return dao.updateWidget(widget)
        .doOnComplete { widgetUpdater.update(widget) }
  }

  fun updateWidgetName(widgetName: String) {
    widgetNameSubject.onNext(widgetName)
  }

  fun insertPackageMatcher(packageMatcher: String): Completable {
    return filterDao.insertFilters(appWidgetId, listOf(packageMatcher))
  }

  @CheckReturnValue
  fun findAndInsertMatchingApps(): Completable {
    return filterDao.findFiltersByWidgetId(appWidgetId)
        .firstOrError()
        .flatten()
        .flatMapCompletable { packageMatcher ->
          findMatchingPackages(packageMatcher)
              .toList()
              .flatMapCompletable {
                dao.insertApps(appWidgetId, packageMatcher, it)
              }
        }
  }

  private fun findMatchingPackages(packageMatcher: String): Observable<String> {
    return Observable.fromIterable(allLauncherPackages())
        .filter(matchPackage(packageMatcher))
  }

  @CheckReturnValue
  fun findPossiblePackageMatchers(): Observable<Collection<String>> {
    return Observable.fromCallable {
      findPossiblePackageMatchersSync(allLauncherPackages())
    }
  }

  private fun allLauncherPackages(): List<String> {
    val intent = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
    return packageManager.queryIntentActivities(intent, 0)
        .map { it.activityInfo.applicationInfo.packageName }
        .toSortedSet()
        .toList()
  }

  fun currentWidgetName(): Maybe<String> =
      dao.findWidgetById(appWidgetId)
          .map { it.name }

  @VisibleForTesting
  fun findPossiblePackageMatchersSync(packageNames: List<String>): Set<String> {
    return packageNames
        .flatMap {
          var packageName = it

          it.split('.')
              .foldRight(setOf(it)) { part, acc ->
                packageName = packageName.removeSuffix(".$part")
                acc + "$packageName.*"
              }
              .reversed()
        }
        .toSet()
  }

  fun packageMatchers(): Flowable<List<String>> = filterDao.findFiltersByWidgetId(appWidgetId)

  fun release() = disposables.dispose()

}
