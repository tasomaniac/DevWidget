package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.*;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import java.util.List;

@android.arch.persistence.room.Dao
public interface Dao {

  @Transaction
  @Query("SELECT * FROM widget")
  Flowable<List<WidgetAndPackageNames>> allWidgetsFlowable();

  @Query("SELECT * FROM widget")
  Single<List<Widget>> allWidgetsSingle();

  @Query("SELECT * FROM widget WHERE appWidgetId = :appWidgetId")
  Maybe<Widget> findWidgetById(int appWidgetId);

  @Query("SELECT * from widget where appWidgetId IN (:appWidgetId)")
  Single<List<Widget>> findWidgetsById(int... appWidgetId);

  @Query("SELECT DISTINCT packageName FROM app WHERE appWidgetId = :appWidgetId")
  List<String> findAppsByWidgetIdSync(int appWidgetId);

  @Query("DELETE FROM app WHERE packageName = :packageName")
  void deleteAppSync(String packageName);

  @Insert(onConflict = OnConflictStrategy.FAIL)
  void insertWidgetSync(Widget... widget);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void updateWidgetSync(Widget widget);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insertAppSync(List<App> app);

  @Delete
  void deleteWidgetsSync(Widget... widgets);
}
