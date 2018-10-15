package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface AppDao {

    @Query("SELECT DISTINCT packageName FROM app WHERE appWidgetId = :appWidgetId")
    fun findAppsByWidgetIdSync(appWidgetId: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertApp(app: App): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertApps(app: List<App>): Completable

    @Query("DELETE FROM app WHERE packageName = :packageName")
    fun deleteAppSync(packageName: String)

    @Query("DELETE FROM app WHERE packageMatcher = :packageMatcher")
    fun deleteAppsByPackageMatcherSync(packageMatcher: String)
}
