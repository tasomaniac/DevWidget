package com.tasomaniac.devwidget.main

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.tasomaniac.devwidget.LifecycleScopeModule
import com.tasomaniac.devwidget.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [LifecycleScopeModule::class])
interface MainModule {

    @Binds
    fun fragmentActivity(activity: MainActivity): FragmentActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainModel::class)
    fun mainModel(mainModel: MainModel): ViewModel
}
