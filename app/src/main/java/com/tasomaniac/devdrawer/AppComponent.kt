package com.tasomaniac.devdrawer

import com.tasomaniac.devdrawer.data.DataModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  AppModule::class,
  BindingModule::class,
  DataModule::class
])
interface AppComponent : AndroidInjector<DevDrawerApp> {

  @Component.Builder
  abstract class Builder : AndroidInjector.Builder<DevDrawerApp>()
}
