package com.tasomaniac.devdrawer;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import com.tasomaniac.devdrawer.rx.SchedulingStrategy;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
abstract class AppModule {

    @Binds
    abstract Application application(DevDrawerApp devDrawerApp);

    @Provides
    static PackageManager packageManager(Application app) {
        return app.getPackageManager();
    }

    @Provides
    static AppWidgetManager appWidgetManager(Application app) {
        return (AppWidgetManager) app.getSystemService(Context.APPWIDGET_SERVICE);
    }

    @Provides
    static SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    static Resources resources(Application app) {
        return app.getResources();
    }

    @Provides
    static SchedulingStrategy schedulingStrategy() {
        return new SchedulingStrategy(Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
