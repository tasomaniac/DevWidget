package com.tasomaniac.devwidget

import androidx.fragment.app.FragmentActivity
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import dagger.Module
import dagger.Provides

@Module
object LifecycleScopeModule {

    @Provides
    @JvmStatic
    internal fun lifecycleScopeProvider(activity: FragmentActivity): ScopeProvider =
        AndroidLifecycleScopeProvider.from(activity)
}
