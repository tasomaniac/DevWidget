package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppDao {

  @Query("SELECT DISTINCT packageName FROM app WHERE appWidgetId = :appWidgetId")
  List<String> findAppsByWidgetIdSync(int appWidgetId);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insertAppSync(List<App> app);

  @Query("DELETE FROM app WHERE packageName = :packageName")
  void deleteAppSync(String packageName);

  @Query("DELETE FROM app WHERE packageMatcher = :packageMatcher")
  void deleteAppsByPackageMatcherSync(String packageMatcher);

  @Query("UPDATE app SET appWidgetId = :appWidgetId WHERE appWidgetId = 2147483647")
  void updateTempWidgetIdSync(int appWidgetId);
}
