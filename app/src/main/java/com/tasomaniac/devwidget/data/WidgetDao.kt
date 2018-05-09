package com.tasomaniac.devwidget.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface WidgetDao {

    @Query("SELECT * FROM widget")
    fun allWidgets(): Single<List<Widget>>

    @Query("SELECT * FROM widget WHERE appWidgetId = :appWidgetId")
    fun findWidgetById(appWidgetId: Int): Maybe<Widget>

    @Query("SELECT * from widget where appWidgetId IN (:appWidgetId)")
    fun findWidgetsById(appWidgetId: IntArray): Single<List<Widget>>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertWidgetSync(vararg widget: Widget)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateWidgetSync(widget: Widget)

    @Query("UPDATE widget SET appWidgetId = :appWidgetId WHERE appWidgetId = -1")
    fun updateTempWidgetIdSync(appWidgetId: Int)

    @Delete
    fun deleteWidgetsSync(widgets: List<Widget>)
}
