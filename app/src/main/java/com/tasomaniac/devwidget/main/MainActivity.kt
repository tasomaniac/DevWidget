package com.tasomaniac.devwidget.main

import android.content.Intent
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.settings.SettingsActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.include_appbar.toolbar
import kotlinx.android.synthetic.main.main_activity.mainAddNewWidget
import kotlinx.android.synthetic.main.main_content.mainWidgetList
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {

    @Inject lateinit var presenter: MainPresenter
    @Inject lateinit var widgetListAdapter: WidgetListAdapter

    private var listener: MainView.Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)

        setupList()
        presenter.bind(this)
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

    @RequiresApi(O)
    override fun renderAddWidgetButton() {
        mainAddNewWidget.visibility = View.VISIBLE
        mainAddNewWidget.setOnClickListener {
            listener?.onAddNewWidgetClicked(context = this)
        }
    }

    override fun updateItems(items: List<WidgetListData>, diffCallbacks: WidgetDiffCallbacks) {
        widgetListAdapter.data = items
        diffCallbacks.calculateDiffAndDispatchUpdates(widgetListAdapter)
    }

    override fun setListener(listener: MainView.Listener?) {
        this.listener = listener
    }

    override fun onDestroy() {
        presenter.unbind(this)
        super.onDestroy()
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
