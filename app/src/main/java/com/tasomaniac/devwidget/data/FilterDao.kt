package com.tasomaniac.devwidget.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FilterDao {

    @Query("SELECT * FROM filter")
    fun allFilters(): Single<List<Filter>>

    @Query("SELECT packageMatcher FROM filter WHERE appWidgetId = :appWidgetId")
    fun findFiltersByWidgetId(appWidgetId: Int): Flowable<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilterSync(filter: List<Filter>)

    @Query("DELETE FROM filter WHERE packageMatcher = :packageMatcher")
    fun deleteFilterSync(packageMatcher: String)

}
