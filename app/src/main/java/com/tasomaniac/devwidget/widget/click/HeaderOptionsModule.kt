package com.tasomaniac.devwidget.widget.click

import android.app.Activity
import dagger.Binds
import dagger.Module

@Module
interface HeaderOptionsModule {

    @Binds
    fun activity(activity: HeaderOptionsActivity): Activity
}
