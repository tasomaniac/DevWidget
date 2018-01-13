package com.tasomaniac.devdrawer.configure

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES.O
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import com.tasomaniac.devdrawer.widget.WidgetUpdater
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfigurePresenter @Inject constructor(
    private val useCase: ConfigureUseCase,
    private val widgetUpdater: WidgetUpdater,
    private val configurePinning: ConfigurePinning,
    private val scheduling: SchedulingStrategy) {

  private val disposables = CompositeDisposable()

  fun bind(view: ConfigureView) {
    view.setListener(ViewListener(view, useCase, widgetUpdater, configurePinning, disposables, scheduling))
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
            .compose(scheduling.forObservable())
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
      private val configurePinning: ConfigurePinning,
      private val disposables: CompositeDisposable,
      private val scheduling: SchedulingStrategy
  ) : ConfigureView.Listener {

    override fun onConfirmClicked() {
      val disposable = useCase.findAndInsertMatchingApps()
          .andThen(updateWidget())
          .compose(scheduling.forCompletable())
          .subscribe {
            view.finishWith(useCase.appWidgetId)
          }
      disposables.add(disposable)
    }

    @TargetApi(O)
    private fun updateWidget(): Completable {
      return Completable.fromAction {
        if (configurePinning) {
          widgetUpdater.requestPin()
        } else {
          widgetUpdater.notifyWidgetDataChanged(useCase.appWidgetId)
        }
      }
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
