package com.tasomaniac.devdrawer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    primaryKeys = ["packageName", "appWidgetId"],
    foreignKeys = [ForeignKey(
        entity = Widget::class,
        parentColumns = ["appWidgetId"],
        childColumns = ["appWidgetId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("appWidgetId")]
)
data class App(
    val packageName: String,
    val appWidgetId: Int
)

@Entity
data class Widget(
    @PrimaryKey val appWidgetId: Int,
    val name: String = ""
)
