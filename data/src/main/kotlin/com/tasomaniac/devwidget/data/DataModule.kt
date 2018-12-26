package com.tasomaniac.devwidget.data

import android.app.Application
import androidx.room.Room
import com.tasomaniac.devwidget.data.migration.Migration1to2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Singleton
    @Provides
    @JvmStatic
    fun room(app: Application): Database =
        Room.databaseBuilder(app, Database::class.java, "devwidget")
            .addMigrations(Migration1to2)
            .build()

    @Provides
    @JvmStatic
    fun widgetDao(database: Database) = database.widgetDao()

    @Provides
    @JvmStatic
    fun appDao(database: Database) = database.appDao()

    @Provides
    @JvmStatic
    fun fullWidgetDao(database: Database) = database.fullWidgetDao()

    @Provides
    @JvmStatic
    fun filterDao(database: Database) = database.filterDao()
}
