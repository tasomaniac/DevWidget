package com.tasomaniac.devdrawer.receivers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.FilterDao
import com.tasomaniac.devdrawer.data.insertApp
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.rx.flatten
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import com.tasomaniac.devdrawer.widget.matchPackage
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.Completable
import javax.inject.Inject

class PackageAddedReceiver : DaggerBroadcastReceiver() {

  @Inject lateinit var filterDao: FilterDao
  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var widgetUpdater: WidgetUpdater

  @SuppressLint("CheckResult")
  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    if (intent.action != Intent.ACTION_PACKAGE_ADDED) {
      throw IllegalStateException("Unexpected receiver with action: ${intent.action}")
    }
    val pendingResult = goAsync()
    val installedPackage = intent.data.schemeSpecificPart

    filterDao.allFilters()
        .flatten()
        .filter {
          matchPackage(it.packageMatcher).test(installedPackage)
        }
        .flatMapCompletable {
          appDao.insertApp(it.appWidgetId, it.packageMatcher, installedPackage)
              .andThen(updateWidget(it.appWidgetId))
        }
        .compose(scheduling.forCompletable())
        .subscribe {
          pendingResult.finish()
        }
  }

  private fun updateWidget(appWidgetId: Int): Completable {
    return Completable.fromAction {
      widgetUpdater.notifyWidgetDataChanged(appWidgetId)
    }
  }
}
