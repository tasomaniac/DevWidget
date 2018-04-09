package com.tasomaniac.devwidget.widget

import android.graphics.drawable.Drawable

data class DisplayApplicationInfo(
    val label: CharSequence,
    val packageName: String,
    val icon: Drawable
)

