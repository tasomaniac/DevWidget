package com.tasomaniac.devdrawer.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import com.tasomaniac.devdrawer.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.configure_activity.*
import kotlinx.android.synthetic.main.configure_content.*
import javax.inject.Inject

class ConfigureActivity : DaggerAppCompatActivity(), ConfigureView {

  @Inject lateinit var presenter: ConfigurePresenter

  private lateinit var adapter: ArrayAdapter<String>
  private var listener: ConfigureView.Listener? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setResult(Activity.RESULT_CANCELED)
    setContentView(R.layout.configure_activity)
    setupToolbar()
    setupPackageMatcher()
  }

  private fun setupToolbar() {
    toolbar.setNavigationOnClickListener {
      val packageName = configurePackageName.text.toString()
      val widgetName = configureWidgetName.text.toString()
      listener?.onAddWidgetClicked(widgetName, listOf(packageName))
    }
  }

  private fun setupPackageMatcher() {
    adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1)
    configurePackageName.setAdapter(adapter)
  }

  override fun setWidgetName(widgetName: String) {
    configureWidgetName.setText(widgetName)
  }

  override fun setItems(items: Collection<String>) {
    adapter.addAll(items)
  }

  override fun setListener(listener: ConfigureView.Listener?) {
    this.listener = listener
  }

  override fun finishWith(appWidgetId: Int) {
    val resultValue = Intent()
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    setResult(Activity.RESULT_OK, resultValue)
    finish()
  }

  override fun onStart() {
    super.onStart()
    presenter.bind(this)
  }

  override fun onStop() {
    presenter.unbind(this)
    super.onStop()
  }
}

