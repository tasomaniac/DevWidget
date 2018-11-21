package com.tasomaniac.devwidget.app

import com.tasomaniac.devwidget.data.Analytics
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AnalyticsModule {

    @Binds
    @Singleton
    fun provideAnalytics(analytics: Analytics.DebugAnalytics): Analytics
}
