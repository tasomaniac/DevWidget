package com.tasomaniac.devwidget.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

interface Debouncer<T> : ObservableTransformer<T, T>

class DefaultDebouncer<T>(
    private val timeout: Long,
    private val unit: TimeUnit
) : Debouncer<T> {

  override fun apply(upstream: Observable<T>) = upstream.debounce(timeout, unit)
}

class EmptyDebouncer<T> : Debouncer<T> {
  override fun apply(upstream: Observable<T>) = upstream
}
