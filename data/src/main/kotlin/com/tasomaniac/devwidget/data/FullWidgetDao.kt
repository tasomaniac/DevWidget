package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Flowable

@Dao
interface FullWidgetDao {

    @Transaction
    @Query("SELECT * FROM widget WHERE appWidgetId != -1")
    fun allWidgets(): Flowable<List<FullWidget>>

    @Transaction
    @Query("SELECT * FROM widget WHERE appWidgetId = :appWidgetId")
    fun findWidgetById(appWidgetId: Int): Flowable<FullWidget>
}
