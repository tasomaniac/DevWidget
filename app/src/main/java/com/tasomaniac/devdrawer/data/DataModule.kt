package com.tasomaniac.devdrawer.data

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

  @Singleton
  @Provides
  @JvmStatic
  fun room(app: Application): AppDatabase =
      Room.databaseBuilder(app, AppDatabase::class.java, "devdrawer").build()

  @Provides
  @JvmStatic
  fun appDao(appDatabase: AppDatabase) = appDatabase.appDao()

}
