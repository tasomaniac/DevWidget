package com.tasomaniac.devwidget.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Widget::class, Filter::class, App::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun widgetDao(): WidgetDao

    abstract fun appDao(): AppDao

    abstract fun widgetAppDao(): WidgetAppDao

    abstract fun filterDao(): FilterDao
}
