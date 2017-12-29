package com.tasomaniac.devdrawer.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.insert
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.new_app_widget_configure.*
import javax.inject.Inject

class AppWidgetConfigureActivity : DaggerAppCompatActivity() {

  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy

  private var disposable = Disposables.empty()
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
  private var onClickListener = View.OnClickListener {
    disposable.dispose()
    val packageName = configurePackageName.text.toString()
    disposable = appDao.insert(appWidgetId, packageName)
        .compose(scheduling.forCompletable())
        .subscribe {
          updateWidget()

          val resultValue = Intent()
          resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
          setResult(Activity.RESULT_OK, resultValue)
          finish()
        }
  }

  private fun updateWidget() {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    WidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId)
  }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)

    setContentView(R.layout.new_app_widget_configure)
    configureAdd.setOnClickListener(onClickListener)

    appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}

