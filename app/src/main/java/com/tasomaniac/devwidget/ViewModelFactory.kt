package com.tasomaniac.devwidget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(clazz: Class<T>) = provider(clazz).get() as T

    private fun <T : ViewModel> provider(clazz: Class<T>) = creators[clazz]
        ?: throw IllegalArgumentException("Unrecognised class $clazz")
}
