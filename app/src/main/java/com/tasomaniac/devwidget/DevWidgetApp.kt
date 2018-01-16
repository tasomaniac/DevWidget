package com.tasomaniac.devwidget

import com.tasomaniac.devwidget.settings.NightModePreferences
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

import javax.inject.Inject

class DevWidgetApp : DaggerApplication() {

  @Inject lateinit var nightModePreferences: NightModePreferences

  override fun onCreate() {
    super.onCreate()
    nightModePreferences.updateDefaultNightMode()

    if (!BuildConfig.DEBUG) {
      //            Fabric.with(this, new Crashlytics());
      //            Timber.plant(new CrashReportingTree());
    } else {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun applicationInjector(): AndroidInjector<DevWidgetApp> = DaggerAppComponent.builder().create(this)
}
