package com.tasomaniac.devdrawer.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Relation

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

@Entity(
    primaryKeys = ["packageMatcher", "appWidgetId"],
    foreignKeys = [ForeignKey(
        entity = Widget::class,
        parentColumns = ["appWidgetId"],
        childColumns = ["appWidgetId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("appWidgetId")]
)
data class Filter(
    val packageMatcher: String,
    val appWidgetId: Int
)

@Entity
data class Widget(
    @PrimaryKey val appWidgetId: Int,
    val name: String = ""
)

class WidgetAndPackageNames {
  var appWidgetId: Int = 0
  lateinit var name: String

  @field:Relation(
      entity = App::class,
      parentColumn = "appWidgetId",
      entityColumn = "appWidgetId",
      projection = ["packageName"]
  )
  lateinit var packageNames: List<String>
}
