package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.*;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface WidgetDao {

  @Transaction
  @Query("SELECT * FROM widget")
  Flowable<List<WidgetAndPackageNames>> allWidgetsWithPackages();

  @Query("SELECT appWidgetId FROM widget")
  Single<List<Integer>> allWidgetIds();

  @Query("SELECT * FROM widget WHERE appWidgetId = :appWidgetId")
  Maybe<Widget> findWidgetById(int appWidgetId);

  @Query("SELECT * from widget where appWidgetId IN (:appWidgetId)")
  Single<List<Widget>> findWidgetsById(int... appWidgetId);

  @Insert(onConflict = OnConflictStrategy.FAIL)
  void insertWidgetSync(Widget... widget);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void updateWidgetSync(Widget widget);

  @Delete
  void deleteWidgetsSync(Widget... widgets);
}
