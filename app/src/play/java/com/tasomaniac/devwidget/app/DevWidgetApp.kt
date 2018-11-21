package com.tasomaniac.devwidget.app

import android.util.Log
import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import timber.log.Timber

class DevWidgetApp : BaseDevWidgetApp() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<DevWidgetApp> =
        DaggerAppComponent.builder().create(this)

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            Crashlytics.log(priority, tag, message)
            if (t != null && priority >= Log.WARN) {
                Crashlytics.logException(t)
            }
        }
    }
}
