package com.tasomaniac.devdrawer.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetDataResolver
import com.tasomaniac.devdrawer.widget.WidgetProvider
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_content.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager
  @Inject lateinit var widgetDataResolver: WidgetDataResolver

  private var disposable: Disposable = Disposables.empty()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    setSupportActionBar(toolbar)

    if (SDK_INT >= O) {
      mainAddNewWidget.visibility = View.VISIBLE
      mainAddNewWidget.setOnClickListener {
        requestAddWidget()
      }
    }

    disposable.dispose()
    disposable = dao.allWidgets()
        .flatMapSingle {
          Flowable.fromIterable(it)
              .map { widget ->
                val packageNames = dao.findAppsByWidgetId(widget.appWidgetId)
                widget to packageNames.mapNotNull {
                  widgetDataResolver.resolve(it)
                }
              }
              .toList()
        }
        .compose(scheduling.forFlowable())
        .subscribe {
          mainWidgetList.adapter = WidgetListAdapter(it)
        }
  }

  @RequiresApi(O)
  private fun requestAddWidget() {
    if (appWidgetManager.isRequestPinAppWidgetSupported) {
      val widgetProvider = ComponentName(this, WidgetProvider::class.java)
      appWidgetManager.requestPinAppWidget(widgetProvider, null, null)
    }
  }

  override fun onDestroy() {
    disposable.dispose()
    super.onDestroy()
  }

}
