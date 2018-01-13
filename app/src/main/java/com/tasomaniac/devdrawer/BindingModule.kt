package com.tasomaniac.devdrawer

import com.tasomaniac.devdrawer.configure.ConfigureActivity
import com.tasomaniac.devdrawer.configure.ConfigureModule
import com.tasomaniac.devdrawer.configure.WidgetPinnedReceiver
import com.tasomaniac.devdrawer.main.MainActivity
import com.tasomaniac.devdrawer.receivers.PackageAddedReceiver
import com.tasomaniac.devdrawer.receivers.PackageRemovedReceiver
import com.tasomaniac.devdrawer.widget.WidgetProvider
import com.tasomaniac.devdrawer.widget.WidgetViewsService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BindingModule {

  @ContributesAndroidInjector
  fun packageAddedReceiver(): PackageAddedReceiver

  @ContributesAndroidInjector
  fun packageRemovedReceiver(): PackageRemovedReceiver

  @ContributesAndroidInjector
  fun widgetProvider(): WidgetProvider

  @ContributesAndroidInjector
  fun widgetViewsService(): WidgetViewsService

  @ContributesAndroidInjector(modules = [ConfigureModule::class])
  fun configureActivity(): ConfigureActivity

  @ContributesAndroidInjector
  fun widgetPinnedReceiver(): WidgetPinnedReceiver

  @ContributesAndroidInjector
  fun mainActivity(): MainActivity
}
