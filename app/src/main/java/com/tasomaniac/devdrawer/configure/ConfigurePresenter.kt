package com.tasomaniac.devdrawer.configure

import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfigurePresenter @Inject constructor(
    private val useCase: ConfigureUseCase,
    private val scheduling: SchedulingStrategy) {

  private val disposables = CompositeDisposable()

  fun bind(view: ConfigureView) {
    view.setListener(ViewListener(view, useCase, disposables, scheduling))
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
        useCase.filters()
            .compose(scheduling.forFlowable())
            .subscribe(view::setFilters)
    )
    disposables.add(
        useCase.widgetPublisher
            .subscribe(view::updateWidget)
    )
  }

  fun unbind(view: ConfigureView) {
    disposables.dispose()
    view.setListener(null)
  }

  fun release() {
    useCase.release()
  }

  private class ViewListener(
      private val view: ConfigureView,
      private val useCase: ConfigureUseCase,
      private val disposables: CompositeDisposable,
      private val scheduling: SchedulingStrategy
  ) : ConfigureView.Listener {

    override fun onConfirmClicked() {
      disposables.add(
          useCase.insert()
              .compose(scheduling.forCompletable())
              .subscribe {
                view.finishWith(useCase.appWidgetId)
              }
      )
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
