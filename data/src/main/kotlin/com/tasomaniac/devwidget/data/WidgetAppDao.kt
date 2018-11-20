package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

import io.reactivex.Flowable

@Dao
interface WidgetAppDao {

    @Transaction
    @Query("SELECT * FROM widget WHERE appWidgetId != -1")
    fun allWidgetsWithPackages(): Flowable<List<WidgetAndPackageNames>>
}
