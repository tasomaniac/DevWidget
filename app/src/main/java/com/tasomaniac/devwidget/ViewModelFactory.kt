package com.tasomaniac.devwidget

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(clazz: Class<T>) = provider(clazz).get() as T

    private fun <T : ViewModel> provider(clazz: Class<T>) = creators[clazz]
            ?: throw IllegalArgumentException("Unrecognised class " + clazz)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModelWith(
    factory: ViewModelProvider.Factory = ViewModelProvider.NewInstanceFactory()
): T = ViewModelProviders.of(this, factory).get(T::class.java)
