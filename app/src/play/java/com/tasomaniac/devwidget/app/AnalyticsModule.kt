package com.tasomaniac.devwidget.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics.getInstance
import com.tasomaniac.devwidget.BuildConfig
import com.tasomaniac.devwidget.data.Analytics
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Module
object AnalyticsModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideAnalytics(
        firebase: Provider<FirebaseAnalytics>,
        debug: Provider<Analytics.DebugAnalytics>
    ): Analytics = if (BuildConfig.DEBUG) debug.get() else firebase.get()

    class FirebaseAnalytics @Inject constructor(app: Application) : Analytics {

        private val firebase = getInstance(app)

        override fun sendScreenView(activity: Activity, screenName: String) {
            firebase.setCurrentScreen(activity, screenName, null)
        }

        override fun sendEvent(name: String, vararg params: Pair<String, String>) {
            firebase.logEvent(name, Bundle().apply {
                params.forEach { (key, value) ->
                    putString(key, value)
                }
            })
        }
    }
}
