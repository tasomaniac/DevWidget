package com.tasomaniac.devwidget

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
object ViewModelProviderModule {

    @Provides
    @JvmStatic
    fun viewModelProvider(
        activity: FragmentActivity,
        factory: ViewModelFactory
    ) = ViewModelProviders.of(activity, factory)
}
