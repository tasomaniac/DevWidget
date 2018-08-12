package com.tasomaniac.devwidget

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

class ViewModelProvider @Inject constructor(
    val activity: FragmentActivity,
    val factory: ViewModelFactory
) {

    inline fun <reified T : ViewModel> get(): T =
        ViewModelProviders.of(activity, factory).get(T::class.java)
}
