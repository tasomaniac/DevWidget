package com.tasomaniac.devwidget.configure

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.tasomaniac.devwidget.data.Analytics
import com.tasomaniac.devwidget.widget.preview.WidgetListData
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.configure_activity.configureWidgetPreview
import kotlinx.android.synthetic.main.configure_activity.toolbar
import kotlinx.android.synthetic.main.configure_content.configureNewPackageMatcher
import kotlinx.android.synthetic.main.configure_content.configurePackageMatcherList
import javax.inject.Inject

internal class ConfigureActivity : DaggerAppCompatActivity(), ConfigureView {

    @Inject lateinit var presenter: ConfigurePresenter
    @Inject lateinit var widgetPreview: WidgetPreview
    @Inject lateinit var packageMatcherListAdapter: PackageMatcherListAdapter
    @Inject lateinit var analytics: Analytics

    private lateinit var adapter: ArrayAdapter<String>

    val configurePin: ConfigurePinning
        get() = intent.getBooleanExtra(EXTRA_SHOULD_PIN, false)

    override var onConfirmClicked: () -> Unit = {}
    override var onSettingsClicked: () -> Unit = {}
    override var onPackageMatcherAdded: (String) -> Unit = {}

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.configure_activity)
        setupToolbar()
        setupNewPackageMatcher()
        setupPackageMatcherList()

        if (savedInstanceState == null) analytics.sendScreenView(this, "Configure")
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onConfirmClicked()
        }
        toolbar.inflateMenu(R.menu.configure_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> onSettingsClicked()
            }
            true
        }
    }

    private fun setupNewPackageMatcher() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1)
        configureNewPackageMatcher.setAdapter(adapter)

        val onPackageMatcherAdded: (String) -> Unit = {
            onPackageMatcherAdded(it).also {
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

    override fun updateWidgetPreview(widgetListData: WidgetListData) {
        widgetPreview.updateWidgetPreview(configureWidgetPreview, widgetListData)
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

    companion object {

        private const val EXTRA_SHOULD_PIN = "EXTRA_SHOULD_PIN"

        @RequiresApi(O)
        fun createIntentForPinning(context: Context) =
            Intent(context, ConfigureActivity::class.java)
                .putExtra(EXTRA_SHOULD_PIN, true)
    }
}
