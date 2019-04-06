package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FilterDao {

    @Query("SELECT * FROM filter")
    fun allFilters(): Single<List<Filter>>

    @Query("SELECT packageMatcher FROM filter WHERE appWidgetId = :appWidgetId")
    fun findFiltersByWidgetId(appWidgetId: Int): Flowable<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilter(filter: Filter): Completable

    @Query("DELETE FROM filter WHERE packageMatcher = :packageMatcher")
    fun deletePackageMatcher(packageMatcher: String): Completable
}
