package com.tasomaniac.devwidget.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface WidgetDao {

  @Query("SELECT * FROM widget")
  Single<List<Widget>> allWidgets();

  @Query("SELECT * FROM widget WHERE appWidgetId = :appWidgetId")
  Maybe<Widget> findWidgetById(int appWidgetId);

  @Query("SELECT * from widget where appWidgetId IN (:appWidgetId)")
  Single<List<Widget>> findWidgetsById(int... appWidgetId);

  @Insert(onConflict = OnConflictStrategy.FAIL)
  void insertWidgetSync(Widget... widget);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void updateWidgetSync(Widget widget);

  @Query("UPDATE widget SET appWidgetId = :appWidgetId WHERE appWidgetId = -1")
  void updateTempWidgetIdSync(int appWidgetId);

  @Delete
  void deleteWidgetsSync(List<Widget> widgets);
}
