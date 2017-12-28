package com.tasomaniac.devdrawer.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.App
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import kotlinx.android.synthetic.main.new_app_widget_configure.*
import javax.inject.Inject

class AppWidgetConfigureActivity : DaggerAppCompatActivity() {

  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy

  private var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
  private var mOnClickListener: View.OnClickListener = View.OnClickListener {
    Completable
        .fromAction {
          appDao.insert(App(appwidget_text.text.toString(), mAppWidgetId))
        }
        .compose(scheduling.forCompletable())
        .subscribe {
          updateWidget()

          val resultValue = Intent()
          resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
          setResult(Activity.RESULT_OK, resultValue)
          finish()
        }
  }

  private fun updateWidget() {
    val appWidgetManager = AppWidgetManager.getInstance(this)
    WidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId)
  }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)

    setContentView(R.layout.new_app_widget_configure)
    findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

    mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }
  }
}

