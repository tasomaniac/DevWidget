package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable

@Dao
interface FavActionDao {

    @Query("SELECT `action` FROM favAction WHERE appWidgetId = :appWidgetId")
    fun findFavActionByWidgetIdSync(appWidgetId: Int): Action

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavAction(favAction: FavAction): Completable
}
