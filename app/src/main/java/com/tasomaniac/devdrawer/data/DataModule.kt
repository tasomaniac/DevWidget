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
  fun room(app: Application): Database =
      Room.databaseBuilder(app, Database::class.java, "devdrawer").build()

  @Provides
  @JvmStatic
  fun widgetDao(database: Database) = database.widgetDao()

  @Provides
  @JvmStatic
  fun appDao(database: Database) = database.appDao()

  @Provides
  @JvmStatic
  fun filterDao(database: Database) = database.filterDao()

}
