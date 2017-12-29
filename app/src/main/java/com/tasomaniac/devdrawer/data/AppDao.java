package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface AppDao {

    @Query("SELECT packageName from app where appWidgetId = :appWidgetId")
    List<String> findAppsByWidgetId(int appWidgetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSync(App app);

    @Query("DELETE FROM app where appWidgetId IN (:appWidgetIds)")
    void deleteWidgetsSync(int... appWidgetIds);
}
