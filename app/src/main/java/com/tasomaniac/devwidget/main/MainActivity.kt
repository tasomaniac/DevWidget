package com.tasomaniac.devwidget.main

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.ViewModelProvider
import com.tasomaniac.devwidget.data.Analytics
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import com.tasomaniac.devwidget.settings.SettingsActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.include_appbar.toolbar
import kotlinx.android.synthetic.main.main_content.mainEmptyInfo
import kotlinx.android.synthetic.main.main_content.mainWidgetList
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelProvider: ViewModelProvider
    @Inject lateinit var scopeProvider: AndroidLifecycleScopeProvider
    @Inject lateinit var navigation: MainNavigation
    @Inject lateinit var widgetListAdapter: WidgetListAdapter
    @Inject lateinit var appWidgetManager: AppWidgetManager
    @Inject lateinit var scheduling: SchedulingStrategy
    @Inject lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        setupList()

        if (isPinningSupported()) {
            val mainAddNewWidget = findViewById<View>(R.id.mainAddNewWidget)
            mainAddNewWidget.visibility = View.VISIBLE
            mainAddNewWidget.setOnClickListener {
                navigation.navigateForPinning(this)
            }
        }

        viewModelProvider.get<MainModel>()
            .data
            .compose(scheduling.forFlowable())
            .autoDisposable(scopeProvider)
            .subscribe { (data, diff) ->
                updateEmptyView(data)
                widgetListAdapter.data = data
                diff.dispatchUpdatesTo(widgetListAdapter)
            }

        if (savedInstanceState == null) analytics.sendScreenView(this, "Main")
    }

    private fun updateEmptyView(data: List<WidgetListData>) {
        if (data.isEmpty()) {
            mainEmptyInfo.visibility = View.VISIBLE
            mainEmptyInfo.setText(
                if (isPinningSupported())
                    R.string.main_empty_info_with_pinning
                else
                    R.string.main_empty_info
            )
        } else {
            mainEmptyInfo.visibility = View.GONE
        }
    }

    private fun isPinningSupported() =
        SDK_INT >= O && appWidgetManager.isRequestPinAppWidgetSupported

    private fun setupList() {
        mainWidgetList.adapter = widgetListAdapter
        mainWidgetList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> false
    }
}
