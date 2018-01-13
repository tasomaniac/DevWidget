package com.tasomaniac.devdrawer.main

import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.tasomaniac.devdrawer.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_content.*
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
    mainWidgetList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
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
}
