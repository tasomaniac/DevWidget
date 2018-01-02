package com.tasomaniac.devdrawer.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.insert
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.configure_activity.*
import kotlinx.android.synthetic.main.configure_content.*
import javax.inject.Inject

class ConfigureActivity : DaggerAppCompatActivity(), ConfigureView {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var presenter: ConfigurePresenter

  private lateinit var adapter: ArrayAdapter<String>

  private var disposable = Disposables.empty()
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)
    setContentView(R.layout.configure_activity)

    appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    toolbar.setNavigationOnClickListener {
      addWidget()
    }
    setupPackageMatcher()
  }

  private fun setupPackageMatcher() {
    adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1)
    configurePackageName.setAdapter(adapter)
  }

  override fun setItems(items: Collection<String>) {
    adapter.addAll(items)
  }

  override fun onStart() {
    super.onStart()
    presenter.bind(this)
  }

  override fun onStop() {
    presenter.unbind(this)
    super.onStop()
  }

  private fun addWidget() {
    disposable.dispose()
    val packageName = configurePackageName.text.toString()
    val widgetName = configureWidgetName.text.toString()
    disposable = dao.insert(Widget(appWidgetId, widgetName), packageName)
        .compose(scheduling.forCompletable())
        .subscribe {
          val resultValue = Intent()
          resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
          setResult(Activity.RESULT_OK, resultValue)
          finish()
        }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }
}

