package com.tasomaniac.devdrawer.receivers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.deleteApp
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.DaggerBroadcastReceiver
import io.reactivex.Completable
import javax.inject.Inject

class PackageRemovedReceiver : DaggerBroadcastReceiver() {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager
  
  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    if (intent.action != Intent.ACTION_PACKAGE_REMOVED) {
      throw IllegalStateException("Unexpected receiver with action: ${intent.action}")
    }
    val uninstalledPackage = intent.data.schemeSpecificPart

    dao.deleteApp(uninstalledPackage)
        .andThen(updateAllWidgets())
        .compose(scheduling.forCompletable())
        .subscribe()
  }

  private fun updateAllWidgets(): Completable {
    return dao.allWidgetsSingle()
        .flatMapCompletable {
          Completable.fromAction {
            val appWidgetIds = it.map(Widget::appWidgetId).toIntArray()
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetAppList)
          }
        }
  }
}
