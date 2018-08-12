package com.tasomaniac.devwidget.data

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Singleton
    @Provides
    @JvmStatic
    fun room(app: Application): Database =
        Room.databaseBuilder(app, Database::class.java, "devwidget").build()

    @Provides
    @JvmStatic
    fun widgetDao(database: Database) = database.widgetDao()

    @Provides
    @JvmStatic
    fun appDao(database: Database) = database.appDao()

    @Provides
    @JvmStatic
    fun widgetAppDao(database: Database) = database.widgetAppDao()

    @Provides
    @JvmStatic
    fun filterDao(database: Database) = database.filterDao()

}
