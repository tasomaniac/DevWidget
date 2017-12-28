package com.tasomaniac.devdrawer.data

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["packageName", "appWidgetId"])
data class App(
    val packageName: String,
    val appWidgetId: Int
)
