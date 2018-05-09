package com.tasomaniac.devwidget.widget.click;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ClickHandlingModule {

    @Binds
    abstract Activity activity(ClickHandlingActivity activity);

    @Provides
    static ClickHandlingActivity.Input input(ClickHandlingActivity activity) {
        return activity.getIntent().getParcelableExtra(ClickHandlingActivity.EXTRA_INPUT);
    }
}
