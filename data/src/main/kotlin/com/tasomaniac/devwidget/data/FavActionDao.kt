package com.tasomaniac.devwidget.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Completable

@Dao
interface FavActionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavAction(favAction: FavAction): Completable
}
