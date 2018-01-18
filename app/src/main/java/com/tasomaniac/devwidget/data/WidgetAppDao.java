package com.tasomaniac.devwidget.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface WidgetAppDao {

    @Transaction
    @Query("SELECT * FROM widget WHERE appWidgetId != -1")
    Flowable<List<WidgetAndPackageNames>> allWidgetsWithPackages();
}
