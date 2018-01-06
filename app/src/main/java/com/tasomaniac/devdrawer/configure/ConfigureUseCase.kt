package com.tasomaniac.devdrawer.configure

import android.content.Intent
import android.content.pm.PackageManager
import android.support.annotation.VisibleForTesting
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.insertApps
import com.tasomaniac.devdrawer.data.insertFilters
import com.tasomaniac.devdrawer.data.insertWidget
import com.tasomaniac.devdrawer.data.updateWidget
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.matchPackage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigureUseCase @Inject constructor(
    private val packageManager: PackageManager,
    private val dao: Dao,
    val appWidgetId: Int,
    scheduling: SchedulingStrategy
) {

  private val disposables = CompositeDisposable()
  private val widgetNameSubject: BehaviorSubject<String> = BehaviorSubject.create()
  val widgetPublisher: PublishSubject<Widget> = PublishSubject.create<Widget>()

  init {
    disposables.add(insertIfNotFound()
        .andThen(widgetNameSubject
            .distinctUntilChanged()
            .debounce(1, TimeUnit.SECONDS)
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
        .doOnComplete { widgetPublisher.onNext(widget) }
  }

  fun updateWidgetName(widgetName: String) {
    widgetNameSubject.onNext(widgetName)
  }

  fun insertPackageMatcher(packageMatcher: String): Completable {
    return dao.insertFilters(appWidgetId, listOf(packageMatcher))
  }

  @CheckReturnValue
  fun insert(): Completable {
    val packageMatchers = emptyList<String>()

    return dao.insertFilters(appWidgetId, packageMatchers)
        .andThen(
            Observable.fromIterable(packageMatchers)
                .flatMapCompletable { packageMatcher ->
                  findMatchingPackages(packageMatcher)
                      .toList()
                      .flatMapCompletable {
                        dao.insertApps(appWidgetId, it)
                      }
                }
        )
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

  fun filters(): Flowable<List<String>> = dao.findFiltersByWidgetId(appWidgetId)

  fun release() = disposables.dispose()

}
