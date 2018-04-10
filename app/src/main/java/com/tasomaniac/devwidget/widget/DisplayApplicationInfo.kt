package com.tasomaniac.devwidget.widget

import android.graphics.drawable.Drawable
import android.os.UserHandle

data class DisplayApplicationInfo(
    val label: CharSequence,
    val packageName: String,
    val icon: Drawable,
    val user: UserHandle
)

