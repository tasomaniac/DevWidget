package com.tasomaniac.devdrawer.configure

import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.FilterDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.data.insertApps
import com.tasomaniac.devdrawer.data.insertPackageMatchers
import com.tasomaniac.devdrawer.data.insertWidget
import com.tasomaniac.devdrawer.data.updateWidget
import com.tasomaniac.devdrawer.rx.Debouncer
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.rx.flatten
import com.tasomaniac.devdrawer.rx.onlyTrue
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import com.tasomaniac.devdrawer.widget.matchPackage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConfigureUseCase @Inject constructor(
    private val packageResolver: PackageResolver,
    private val widgetDao: WidgetDao,
    private val appDao: AppDao,
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
    return widgetDao.findWidgetById(appWidgetId)
        .isEmpty.onlyTrue()
        .flatMapCompletable {
          widgetDao.insertWidget(Widget(appWidgetId))
        }
  }

  private fun updateWidget(widgetName: String): Completable {
    val widget = Widget(appWidgetId, widgetName)
    return widgetDao.updateWidget(widget)
        .andThen(widgetUpdater.update(widget))
  }

  fun updateWidgetName(widgetName: String) {
    widgetNameSubject.onNext(widgetName)
  }

  @CheckReturnValue
  fun currentWidgetName() = widgetDao.findWidgetById(appWidgetId).map { it.name }

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

  fun release() = disposables.dispose()

}
