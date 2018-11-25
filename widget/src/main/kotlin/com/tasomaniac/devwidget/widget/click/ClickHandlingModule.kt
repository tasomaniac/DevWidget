package com.tasomaniac.devwidget.widget.click

import androidx.fragment.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal abstract class ClickHandlingModule {

    @Binds
    abstract fun activity(activity: ClickHandlingActivity): FragmentActivity

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun input(activity: ClickHandlingActivity) = activity.intent.input
    }
}
