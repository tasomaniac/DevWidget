package com.tasomaniac.devwidget.main

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.ViewModelProvider
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import com.tasomaniac.devwidget.settings.SettingsActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.include_appbar.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_content.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelProvider: ViewModelProvider
    @Inject lateinit var scopeProvider: AndroidLifecycleScopeProvider
    @Inject lateinit var navigation: MainNavigation
    @Inject lateinit var widgetListAdapter: WidgetListAdapter
    @Inject lateinit var appWidgetManager: AppWidgetManager
    @Inject lateinit var scheduling: SchedulingStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        setupList()

        if (SDK_INT >= O && appWidgetManager.isRequestPinAppWidgetSupported) {
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
                widgetListAdapter.data = data
                diff.dispatchUpdatesTo(widgetListAdapter)
            }
    }

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
