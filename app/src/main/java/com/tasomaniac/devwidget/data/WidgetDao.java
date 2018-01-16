package com.tasomaniac.devwidget.data;

import android.arch.persistence.room.*;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface WidgetDao {

  @Transaction
  @Query("SELECT * FROM widget WHERE appWidgetId != -1")
  Flowable<List<WidgetAndPackageNames>> allWidgetsWithPackages();

  @Query("SELECT appWidgetId FROM widget")
  Single<List<Integer>> allWidgetIds();

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
