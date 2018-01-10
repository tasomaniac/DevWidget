package com.tasomaniac.devdrawer.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface AppDao {

  @Query("DELETE FROM app WHERE packageMatcher = :packageMatcher")
  void deleteAppsByPackageMatcherSync(String packageMatcher);
}
