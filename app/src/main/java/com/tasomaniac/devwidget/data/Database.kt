package com.tasomaniac.devwidget.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Widget::class, Filter::class, App::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

  abstract fun widgetDao(): WidgetDao

  abstract fun appDao(): AppDao

  abstract fun filterDao(): FilterDao
}
