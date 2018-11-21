package com.tasomaniac.devwidget;

import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import androidx.fragment.app.FragmentActivity;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LifecycleScopeModule {

    @Provides
    static AndroidLifecycleScopeProvider lifecycleScopeProvider(FragmentActivity activity) {
        return AndroidLifecycleScopeProvider.from(activity);
    }
}
