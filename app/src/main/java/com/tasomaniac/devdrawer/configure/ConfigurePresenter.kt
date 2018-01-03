package com.tasomaniac.devdrawer.configure

import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfigurePresenter @Inject constructor(
    private val useCase: ConfigureUseCase,
    private val scheduling: SchedulingStrategy
) {

  private var disposables = CompositeDisposable()

  fun bind(view: ConfigureView) {
    view.setListener(ViewListener(view, useCase, disposables, scheduling))
    disposables.add(
        useCase.findPossiblePackageMatchers()
            .subscribe(view::setItems)
    )
  }

  fun unbind(view: ConfigureView) {
    disposables.dispose()
    view.setListener(null)
  }

  private class ViewListener(
      private val view: ConfigureView,
      private val useCase: ConfigureUseCase,
      private val disposables: CompositeDisposable,
      private val scheduling: SchedulingStrategy
  ) : ConfigureView.Listener {

    override fun onAddWidgetClicked(widgetName: String, filters: List<String>) {
      disposables.add(
          useCase.insert(widgetName, filters)
              .compose(scheduling.forCompletable())
              .subscribe {
                view.finishWith(useCase.appWidgetId)
              }
      )
    }
  }
}
