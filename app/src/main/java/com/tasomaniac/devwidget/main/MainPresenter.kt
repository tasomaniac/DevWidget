package com.tasomaniac.devwidget.main

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.support.annotation.RequiresApi
import com.tasomaniac.devwidget.configure.ConfigureActivity
import com.tasomaniac.devwidget.rx.SchedulingStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val useCase: MainUseCase,
    private val scheduling: SchedulingStrategy,
    private val appWidgetManager: AppWidgetManager) {

  private val disposables = CompositeDisposable()

  fun bind(view: MainView) {
    view.setListener(ViewListener())

    if (SDK_INT >= O && appWidgetManager.isRequestPinAppWidgetSupported) {
      view.renderAddWidgetButton()
    }

    val initialPair = emptyList<WidgetListData>() to WidgetDiffCallbacks.EMPTY
    val disposable = useCase.observeWidgets()
        .scan(initialPair) { (data, _), newData ->
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

  class ViewListener : MainView.Listener {

    @RequiresApi(O)
    override fun onAddNewWidgetClicked(context: Context) {
      val intent = ConfigureActivity.createIntentForPinning(context)
      context.startActivity(intent)
    }
  }

}
