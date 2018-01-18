package com.tasomaniac.devwidget

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import javax.inject.Inject

class ViewModelProvider @Inject constructor(
    val activity: FragmentActivity,
    val factory: ViewModelFactory
) {

    inline fun <reified T : ViewModel> get(): T =
        ViewModelProviders.of(activity, factory).get(T::class.java)
}
