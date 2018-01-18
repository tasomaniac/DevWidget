package com.tasomaniac.devwidget;

import android.support.v4.app.FragmentActivity;

import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LifecycleScopeModule {

    @Provides
    static AndroidLifecycleScopeProvider lifecycleScopeProvider(
            FragmentActivity activity) {
        return AndroidLifecycleScopeProvider.from(activity);
    }
}
