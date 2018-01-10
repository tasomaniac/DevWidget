package com.tasomaniac.devdrawer.receivers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.WidgetDao
import com.tasomaniac.devdrawer.data.deleteApp
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.Completable
import javax.inject.Inject

class PackageRemovedReceiver : DaggerBroadcastReceiver() {

  @Inject lateinit var widgetDao: WidgetDao
  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager
  
  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    if (intent.action != Intent.ACTION_PACKAGE_REMOVED) {
      throw IllegalStateException("Unexpected receiver with action: ${intent.action}")
    }
    val uninstalledPackage = intent.data.schemeSpecificPart

    appDao.deleteApp(uninstalledPackage)
        .andThen(updateAllWidgets())
        .compose(scheduling.forCompletable())
        .subscribe()
  }

  private fun updateAllWidgets(): Completable {
    return widgetDao.allWidgetIds()
        .map { it.toIntArray() }
        .flatMapCompletable {
          Completable.fromAction {
            appWidgetManager.notifyAppWidgetViewDataChanged(it, R.id.widgetAppList)
          }
        }
  }
}
