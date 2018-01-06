package com.tasomaniac.devdrawer.rx

import io.reactivex.Single

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Single<List<T>>.flatten() = flattenAsObservable { it }
