package com.tasomaniac.devdrawer.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build.VERSION_CODES.O
import android.support.annotation.RequiresApi
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val useCase: MainUseCase,
    private val scheduling: SchedulingStrategy,
    private val appWidgetManager: AppWidgetManager) {

  private val disposables = CompositeDisposable()

  fun bind(view: MainView) {
    view.setListener(ViewListener(appWidgetManager))
    val initialPair = emptyList<WidgetListData>() to WidgetDiffCallbacks.EMPTY

    val disposable = useCase.observeWidgets()
        .scan(initialPair) { (data, _), newItem ->
          val newData = data.plusOrUpdated(newItem)
          newData to WidgetDiffCallbacks(data, newData)
        }
        .compose(scheduling.forFlowable())
        .subscribe { (data, callbacks) ->
          view.updateItems(data, callbacks)
        }
    disposables.add(disposable)
  }

  fun unbind(view: MainView) {
    disposables.clear()
    view.setListener(null)
  }

  class ViewListener(private val appWidgetManager: AppWidgetManager) : MainView.Listener {

    @RequiresApi(O)
    override fun onAddNewWidgetClicked(context: Context) {
      if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val widgetProvider = ComponentName(context, WidgetProvider::class.java)
        appWidgetManager.requestPinAppWidget(widgetProvider, null, null)
      }
    }
  }

  companion object {

    private fun List<WidgetListData>.plusOrUpdated(newItem: WidgetListData): List<WidgetListData> {
      if (contains(newItem)) {
        return this
      }
      val index = indexOfFirst { it.widget.appWidgetId == newItem.widget.appWidgetId }
      val newData = ArrayList(this)
      if (index == -1) {
        newData.add(newItem)
      } else {
        newData.removeAt(index)
        newData.add(index, newItem)
      }
      return newData
    }
  }

}
