package com.tasomaniac.devwidget.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

import io.reactivex.Flowable

@Dao
interface WidgetAppDao {

    @Transaction
    @Query("SELECT * FROM widget WHERE appWidgetId != -1")
    fun allWidgetsWithPackages(): Flowable<List<WidgetAndPackageNames>>
}
