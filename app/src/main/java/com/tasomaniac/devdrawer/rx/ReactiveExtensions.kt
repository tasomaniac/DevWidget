package com.tasomaniac.devdrawer.rx

import android.annotation.SuppressLint
import io.reactivex.Flowable
import io.reactivex.Single

fun <T> Single<List<T>>.flatten() = flattenAsObservable { it }

@SuppressLint("CheckResult")
fun <T> Flowable<List<T>>.flatten() = flatMap { Flowable.fromIterable(it) }
