package com.tasomaniac.devwidget.configure

import android.appwidget.AppWidgetManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.tasomaniac.devwidget.LifecycleScopeModule
import com.tasomaniac.devwidget.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

typealias ConfigurePinning = Boolean

@Module(
    includes = [
        ActivityBindingModule::class,
        ConfigureViewModelModule::class,
        LifecycleScopeModule::class
    ]
)
object ConfigureModule {

    @Provides
    @JvmStatic
    fun appWidgetId(activity: ConfigureActivity): Int {
        val appWidgetId = activity.intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            throw IllegalArgumentException("AppWidgetManager.EXTRA_APPWIDGET_ID is required.")
        }
        return appWidgetId
    }

    @Provides
    @JvmStatic
    fun configurePinning(activity: ConfigureActivity) = activity.configurePin
}

@Module
interface ActivityBindingModule {

    @Binds
    fun fragmentActivity(activity: ConfigureActivity): FragmentActivity
}

@Module
interface ConfigureViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WidgetNameModel::class)
    fun widgetNameModel(model: WidgetNameModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PackageMatcherModel::class)
    fun packageMatcherModel(model: PackageMatcherModel): ViewModel
}
