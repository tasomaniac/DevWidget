package com.tasomaniac.devdrawer;

import com.tasomaniac.devdrawer.settings.NightModePreferences;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

import javax.inject.Inject;

public class DevDrawerApp extends DaggerApplication {

    @Inject
    NightModePreferences nightModePreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        nightModePreferences.updateDefaultNightMode();

        if (!BuildConfig.DEBUG) {
//            Fabric.with(this, new Crashlytics());
//            Timber.plant(new CrashReportingTree());
        } else {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected AndroidInjector<DevDrawerApp> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
