package com.tasomaniac.devwidget.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.data.Analytics
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.configure_activity.toolbar
import kotlinx.android.synthetic.main.configure_content.configureNewPackageMatcher
import kotlinx.android.synthetic.main.configure_content.configurePackageMatcherList
import kotlinx.android.synthetic.main.configure_content.configureWidgetName
import javax.inject.Inject

class ConfigureActivity : DaggerAppCompatActivity(), ConfigureView {

    @Inject lateinit var presenter: ConfigurePresenter
    @Inject lateinit var packageMatcherListAdapter: PackageMatcherListAdapter
    @Inject lateinit var analytics: Analytics

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var textWatcher: TextWatcher

    override var onConfirmClicked: () -> Unit = {}
    override var widgetNameChanged: (String) -> Unit = {}
    override var onPackageMatcherAdded: (String) -> Unit = {}

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configure_activity)
        toolbar.setNavigationOnClickListener {
            onConfirmClicked()
        }
        setupWidgetName()
        setupNewPackageMatcher()
        setupPackageMatcherList()

        if (savedInstanceState == null) analytics.sendScreenView(this, "Configure")
    }

    private fun setupWidgetName() {
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                widgetNameChanged(text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

        }
        configureWidgetName.addTextChangedListener(textWatcher)
    }

    private fun setupNewPackageMatcher() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1)
        configureNewPackageMatcher.setAdapter(adapter)

        val onPackageMatcherAdded: (String) -> Unit = {
            onPackageMatcherAdded(it).also { _ ->
                configureNewPackageMatcher.text = null
            }
        }
        configureNewPackageMatcher.setOnItemClickListener { _, _, position, _ ->
            onPackageMatcherAdded(adapter.getItem(position)!!)
        }
        configureNewPackageMatcher.setOnEditorActionListener { _, _, _ ->
            onPackageMatcherAdded(configureNewPackageMatcher.text.toString())
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

    override fun onDestroy() {
        configureWidgetName.removeTextChangedListener(textWatcher)
        super.onDestroy()
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
