package com.tasomaniac.devwidget.rx

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import java.util.concurrent.TimeUnit

interface Debouncer<T> : FlowableTransformer<T, T>

class DefaultDebouncer<T>(
    private val timeout: Long,
    private val unit: TimeUnit
) : Debouncer<T> {

    override fun apply(upstream: Flowable<T>) = upstream.debounce(timeout, unit)
}

class EmptyDebouncer<T> : Debouncer<T> {
    override fun apply(upstream: Flowable<T>) = upstream
}
