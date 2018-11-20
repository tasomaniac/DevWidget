package com.tasomaniac.devwidget.widget.click

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ClickHandlingModule {

    @Binds
    abstract fun activity(activity: ClickHandlingActivity): Activity

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun input(activity: ClickHandlingActivity) = activity.intent.input
    }
}
