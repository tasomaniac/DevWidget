package com.tasomaniac.devdrawer.configure

import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfigurePresenter @Inject constructor(
    private val useCase: ConfigureUseCase,
    private val widgetUpdater: WidgetUpdater,
    private val scheduling: SchedulingStrategy) {

  private val disposables = CompositeDisposable()

  fun bind(view: ConfigureView) {
    view.setListener(ViewListener(view, useCase, widgetUpdater, disposables, scheduling))
    disposables.add(
        useCase.findPossiblePackageMatchers()
            .compose(scheduling.forObservable())
            .subscribe(view::setItems)
    )
    disposables.add(
        useCase.currentWidgetName()
            .compose(scheduling.forMaybe())
            .subscribe(view::setWidgetName)
    )
    disposables.add(
        useCase.packageMatchers()
            .compose(scheduling.forFlowable())
            .subscribe(view::setFilters)
    )
  }

  fun unbind(view: ConfigureView) {
    disposables.clear()
    view.setListener(null)
  }

  fun release() {
    useCase.release()
  }

  private class ViewListener(
      private val view: ConfigureView,
      private val useCase: ConfigureUseCase,
      private val widgetUpdater: WidgetUpdater,
      private val disposables: CompositeDisposable,
      private val scheduling: SchedulingStrategy
  ) : ConfigureView.Listener {

    override fun onConfirmClicked() {
      val disposable = useCase.findAndInsertMatchingApps()
          .compose(scheduling.forCompletable())
          .subscribe({
            widgetUpdater.notifyWidgetDataChanged(useCase.appWidgetId)
            view.finishWith(useCase.appWidgetId)
          })
      disposables.add(disposable)
    }

    override fun widgetNameChanged(widgetName: String) {
      useCase.updateWidgetName(widgetName)
    }

    override fun onPackageMatcherAdded(packageMatcher: String) {
      val disposable = useCase.insertPackageMatcher(packageMatcher)
          .compose(scheduling.forCompletable())
          .subscribe()
      disposables.add(disposable)
    }
  }
}
