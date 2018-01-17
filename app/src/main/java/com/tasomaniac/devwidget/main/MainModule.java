package com.tasomaniac.devwidget.main;

import android.arch.lifecycle.ViewModel;

import com.tasomaniac.devwidget.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainModel.class)
    abstract ViewModel mainModel(MainModel mainModel);

}
