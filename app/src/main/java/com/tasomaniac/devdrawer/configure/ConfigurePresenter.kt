package com.tasomaniac.devdrawer.configure

import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ConfigurePresenter @Inject constructor(private val useCase: ConfigureUseCase) {

  private var disposables = CompositeDisposable()

  fun bind(view: ConfigureView) {
    disposables.add(
        useCase.findExistingPackages()
            .subscribe(view::setItems)
    )
  }

  fun unbind(view: ConfigureView) {
    disposables.dispose()
  }
}
