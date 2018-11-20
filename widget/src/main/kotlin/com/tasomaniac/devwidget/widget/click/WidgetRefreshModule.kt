package com.tasomaniac.devwidget.widget.click

import androidx.fragment.app.FragmentActivity
import com.tasomaniac.devwidget.LifecycleScopeModule
import dagger.Binds
import dagger.Module

@Module(includes = [LifecycleScopeModule::class])
interface WidgetRefreshModule {

    @Binds
    fun fragmentActivity(activity: WidgetRefreshActivity): FragmentActivity
}
