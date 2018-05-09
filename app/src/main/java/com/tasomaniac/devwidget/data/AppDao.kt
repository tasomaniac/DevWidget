package com.tasomaniac.devwidget.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface AppDao {

    @Query("SELECT DISTINCT packageName FROM app WHERE appWidgetId = :appWidgetId")
    fun findAppsByWidgetIdSync(appWidgetId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAppSync(app: List<App>)

    @Query("DELETE FROM app WHERE packageName = :packageName")
    fun deleteAppSync(packageName: String)

    @Query("DELETE FROM app WHERE packageMatcher = :packageMatcher")
    fun deleteAppsByPackageMatcherSync(packageMatcher: String)

}
