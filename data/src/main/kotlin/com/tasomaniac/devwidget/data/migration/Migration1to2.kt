package com.tasomaniac.devwidget.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1to2 : Migration(1, 2) {

    @Suppress("MaxLineLength")
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `FavAction` (`action` TEXT NOT NULL, `appWidgetId` INTEGER NOT NULL, PRIMARY KEY(`action`, `appWidgetId`), FOREIGN KEY(`appWidgetId`) REFERENCES `Widget`(`appWidgetId`) ON UPDATE CASCADE ON DELETE CASCADE )")
        database.execSQL("CREATE  INDEX `index_FavAction_appWidgetId` ON `FavAction` (`appWidgetId`)")
    }
}
