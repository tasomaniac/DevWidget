package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface FavActionDao {

    @Query("SELECT `action` FROM favAction WHERE appWidgetId = :appWidgetId")
    fun _findFavActionByWidgetIdSync(appWidgetId: Int): Action?

    @Query("SELECT `action` FROM favAction WHERE appWidgetId = :appWidgetId")
    fun _findFavActionByWidgetId(appWidgetId: Int): Maybe<Action>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavAction(favAction: FavAction): Completable
}
