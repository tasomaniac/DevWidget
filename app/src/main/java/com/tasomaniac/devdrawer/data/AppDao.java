package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppDao {

    @Query("SELECT * from app where appWidgetId = :appWidgetId")
    List<App> findAppsByWidgetId(int appWidgetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(App app);
}
