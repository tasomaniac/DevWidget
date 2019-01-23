package com.tasomaniac.devwidget.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Widget::class, Filter::class, App::class, FavAction::class], version = 2, exportSchema = false)
@TypeConverters(Action.Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun widgetDao(): WidgetDao

    abstract fun appDao(): AppDao

    abstract fun fullWidgetDao(): FullWidgetDao

    abstract fun filterDao(): FilterDao

    abstract fun favActionDao(): FavActionDao
}
