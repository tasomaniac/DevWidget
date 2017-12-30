package com.tasomaniac.devdrawer.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.AppDao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetProvider
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

  @Inject lateinit var appDao: AppDao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    setSupportActionBar(toolbar)

    if (SDK_INT >= O) {
      mainAddNewWidget.visibility = View.VISIBLE
      mainAddNewWidget.setOnClickListener {
        requestAddWidget()
      }
    }


    appDao.allwidgets()
        .compose(scheduling.forFlowable())
        .subscribe {
          mainWidgetList.adapter = WidgetListAdapter(it)
        }
  }

  @RequiresApi(O)
  private fun requestAddWidget() {
    if (appWidgetManager.isRequestPinAppWidgetSupported) {
      val widgetProvider = ComponentName(this, WidgetProvider::class.java)
      appWidgetManager.requestPinAppWidget(widgetProvider, null, null)
    }
  }

  private class WidgetListAdapter(
      private val widgets: List<Widget>
  ) : RecyclerView.Adapter<WidgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
      val view = LayoutInflater.from(parent.context)
          .inflate(android.R.layout.simple_list_item_1, parent, false)
      return WidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
      val appWidgetId = widgets[position].appWidgetId
      (holder.itemView as TextView).text = "$appWidgetId"
    }

    override fun getItemCount() = widgets.size
  }

  private class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
