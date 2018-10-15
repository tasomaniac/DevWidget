package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable

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
    fun insertWidget(vararg widget: Widget): Completable

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateWidget(widget: Widget): Completable

    @Query("UPDATE widget SET appWidgetId = :appWidgetId WHERE appWidgetId = -1")
    fun updateTempWidgetIdSync(appWidgetId: Int)

    @Delete
    fun deleteWidgetsSync(widgets: List<Widget>)
}
