package com.tasomaniac.devdrawer.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [App::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun appDao(): AppDao
}
