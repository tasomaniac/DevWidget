package com.tasomaniac.devwidget.widget.click

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tasomaniac.devwidget.data.updater.WidgetAppsDataUpdater
import com.tasomaniac.devwidget.extensions.SchedulingStrategy
import com.tasomaniac.devwidget.widget.WidgetUpdater
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import javax.inject.Inject

class WidgetRefreshActivity : DaggerAppCompatActivity() {

    @Inject lateinit var widgetAppsDataUpdater: WidgetAppsDataUpdater
    @Inject lateinit var widgetUpdater: WidgetUpdater
    @Inject lateinit var scheduling: SchedulingStrategy
    @Inject lateinit var scopeProvider: AndroidLifecycleScopeProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appWidgetId = intent.getIntExtra(EXTRA_APP_WIDGET_ID, -1)
        widgetAppsDataUpdater
            .findAndInsertMatchingApps(appWidgetId)
            .andThen(Completable.fromAction { widgetUpdater.notifyWidgetDataChanged(appWidgetId) })
            .compose(scheduling.forCompletable())
            .autoDisposable(scopeProvider)
            .subscribe {
                finish()
            }
    }

    companion object {

        private const val EXTRA_APP_WIDGET_ID = "EXTRA_APP_WIDGET_ID"

        fun createIntent(context: Context, appWidgetId: Int) =
            Intent(context, WidgetRefreshActivity::class.java).apply {
                putExtra(EXTRA_APP_WIDGET_ID, appWidgetId)
            }
    }
}
