package com.tasomaniac.devwidget.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(
    primaryKeys = ["packageName", "packageMatcher", "appWidgetId"],
    foreignKeys = [ForeignKey(
        entity = Widget::class,
        parentColumns = ["appWidgetId"],
        childColumns = ["appWidgetId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("appWidgetId")]
)
data class App(
    val packageName: String,
    val packageMatcher: String,
    val appWidgetId: Int
)

@Entity(
    primaryKeys = ["packageMatcher", "appWidgetId"],
    foreignKeys = [ForeignKey(
        entity = Widget::class,
        parentColumns = ["appWidgetId"],
        childColumns = ["appWidgetId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("appWidgetId")]
)
data class Filter(
    val packageMatcher: String,
    val appWidgetId: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Widget::class,
        parentColumns = ["appWidgetId"],
        childColumns = ["appWidgetId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
@TypeConverters(Action.Converters::class)
data class FavAction(
    val action: Action,
    @PrimaryKey val appWidgetId: Int
)

enum class Action {
    UNINSTALL, APP_DETAILS, PLAY_STORE;

    class Converters {

        @TypeConverter
        fun fromAction(action: Action?): String? = action?.name

        @TypeConverter
        fun fromName(name: String?): Action? = if (name == null) null else Action.valueOf(name)
    }
}

@Entity
data class Widget(
    @PrimaryKey val appWidgetId: Int,
    val name: String = ""
)

class FullWidget {
    var appWidgetId: Int = 0
    lateinit var name: String

    @Suppress("PropertyName")
    @field:Relation(
        entity = App::class,
        parentColumn = "appWidgetId",
        entityColumn = "appWidgetId",
        projection = ["packageName"]
    )
    lateinit var _packageNames: List<String>

    val packageNames get() = _packageNames.distinct()

    @field:Relation(
        entity = FavAction::class,
        parentColumn = "appWidgetId",
        entityColumn = "appWidgetId",
        projection = ["action"]
    )
    @TypeConverters(Action.Converters::class)
    @Suppress("PropertyName")
    lateinit var _favAction: List<Action>

    val favAction get() = _favAction.firstOrNull() ?: Action.UNINSTALL
}
