package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.*;
import io.reactivex.Flowable;

import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {

    @Query("SELECT packageName from app where appWidgetId = :appWidgetId")
    List<String> findAppsByWidgetId(int appWidgetId);

    @Query("SELECT * from widget")
    Flowable<List<Widget>> allWidgets();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insertWidgetSync(Widget widget);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSync(App app);

    @Delete
    void deleteWidgetsSync(Widget... widgets);
}
