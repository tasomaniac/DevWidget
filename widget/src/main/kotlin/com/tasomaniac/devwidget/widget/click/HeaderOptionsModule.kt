package com.tasomaniac.devwidget.widget.click

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module

@Module
internal interface HeaderOptionsModule {

    @Binds
    fun activity(activity: HeaderOptionsActivity): FragmentActivity
}
