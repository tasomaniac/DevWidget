package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.*;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface FilterDao {

  @Query("SELECT * FROM filter")
  Single<List<Filter>> allFilters();

  @Query("SELECT packageFilter FROM filter WHERE appWidgetId = :appWidgetId")
  Flowable<List<String>> findFiltersByWidgetId(int appWidgetId);

  @Query("SELECT packageFilter FROM filter WHERE appWidgetId = :appWidgetId")
  Single<List<String>> findFiltersByWidgetIdSingle(int appWidgetId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertFilterSync(List<Filter> filter);

  @Delete
  void deleteFilterSync(Filter... filter);
}
