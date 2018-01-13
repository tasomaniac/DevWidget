package com.tasomaniac.devdrawer.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.tasomaniac.devdrawer.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.configure_activity.*
import kotlinx.android.synthetic.main.configure_content.*
import javax.inject.Inject

class ConfigureActivity : DaggerAppCompatActivity(), ConfigureView {

  @Inject lateinit var presenter: ConfigurePresenter
  @Inject lateinit var packageMatcherListAdapter: PackageMatcherListAdapter

  private lateinit var adapter: ArrayAdapter<String>
  private var listener: ConfigureView.Listener? = null

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.configure_activity)
    setupToolbar()
    setupNewPackageMatcher()
    setupPackageMatcherList()
    configureWidgetName.addTextChangedListener(textWatcher)
  }

  private fun setupToolbar() {
    toolbar.setNavigationOnClickListener {
      listener?.onConfirmClicked()
    }
  }

  private fun setupNewPackageMatcher() {
    adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1)
    configurePackageName.setAdapter(adapter)
    configurePackageName.setOnItemClickListener { _, _, position, _ ->
      val item = adapter.getItem(position)
      listener?.onPackageMatcherAdded(item)
      configurePackageName.text = null
    }
    configurePackageName.setOnEditorActionListener { _, _, _ ->
      listener?.onPackageMatcherAdded(configurePackageName.text.toString())
      configurePackageName.text = null
      true
    }
  }

  private fun setupPackageMatcherList() {
    configurePackageMatcherList.adapter = packageMatcherListAdapter
  }

  override fun setWidgetName(widgetName: String) {
    configureWidgetName.setText(widgetName)
  }

  override fun setFilters(filters: List<String>) {
    packageMatcherListAdapter.data = filters
  }

  override fun setItems(items: Collection<String>) {
    adapter.clear()
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

  override fun onDestroy() {
    presenter.release()
    super.onDestroy()
    configureWidgetName.removeTextChangedListener(textWatcher)
  }

  private val textWatcher = object : TextWatcher {
    override fun afterTextChanged(text: Editable) {
      listener?.widgetNameChanged(text.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

  }

  val configurePin: ConfigurePinning
    get() = intent.getBooleanExtra(EXTRA_SHOULD_PIN, false)

  companion object {

    private const val EXTRA_SHOULD_PIN = "EXTRA_SHOULD_PIN"

    fun createIntent(context: Context, appWidgetId: Int): Intent {
      return Intent(context, ConfigureActivity::class.java)
          .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    }

    @RequiresApi(O)
    fun createIntentForPinning(context: Context): Intent {
      return Intent(context, ConfigureActivity::class.java)
          .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
          .putExtra(EXTRA_SHOULD_PIN, true)
    }
  }
}
