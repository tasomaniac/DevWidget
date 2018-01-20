package com.tasomaniac.devwidget

import dagger.android.AndroidInjector
import timber.log.Timber

class DevWidgetApp : BaseDevWidgetApp() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<DevWidgetApp> =
        DaggerAppComponent.builder().create(this)
}
