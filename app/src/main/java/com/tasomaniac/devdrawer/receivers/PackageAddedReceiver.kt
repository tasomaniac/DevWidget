package com.tasomaniac.devdrawer.receivers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.insertApps
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.matchPackage
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.Completable
import javax.inject.Inject

class PackageAddedReceiver : DaggerBroadcastReceiver() {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    if (intent.action != Intent.ACTION_PACKAGE_ADDED) {
      throw IllegalStateException("Unexpected receiver with action: ${intent.action}")
    }
    val installedPackage = intent.data.schemeSpecificPart

    dao.allFilters()
        .filter {
          matchPackage(it.packageFilter).test(installedPackage)
        }
        .flatMapCompletable {
          dao.insertApps(it.appWidgetId, installedPackage)
              .andThen(updateWidget(it.appWidgetId))
        }
        .compose(scheduling.forCompletable())
        .subscribe()
  }

  private fun updateWidget(appWidgetId: Int): Completable {
    return Completable.fromAction {
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetAppList)
    }
  }
}
