package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.*;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface FilterDao {

  @Query("SELECT * FROM filter")
  Single<List<Filter>> allFilters();

  @Query("SELECT packageMatcher FROM filter WHERE appWidgetId = :appWidgetId")
  Flowable<List<String>> findFiltersByWidgetId(int appWidgetId);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertFilterSync(List<Filter> filter);

  @Query("DELETE FROM filter WHERE packageMatcher = :packageMatcher")
  void deleteFilterSync(String packageMatcher);

}
