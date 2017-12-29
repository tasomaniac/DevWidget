package com.tasomaniac.devdrawer

import com.tasomaniac.devdrawer.main.MainActivity
import com.tasomaniac.devdrawer.widget.AppWidgetConfigureActivity
import com.tasomaniac.devdrawer.widget.WidgetProvider
import com.tasomaniac.devdrawer.widget.WidgetViewsService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BindingModule {

  @ContributesAndroidInjector
  fun widgetProvider(): WidgetProvider

  @ContributesAndroidInjector
  fun widgetViewsService(): WidgetViewsService

  @ContributesAndroidInjector
  fun appWidgetConfigureActivity(): AppWidgetConfigureActivity

  @ContributesAndroidInjector
  fun mainActivity(): MainActivity
}
