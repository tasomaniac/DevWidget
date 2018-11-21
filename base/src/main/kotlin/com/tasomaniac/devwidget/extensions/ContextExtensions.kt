@file:Suppress("NOTHING_TO_INLINE")

package com.tasomaniac.devwidget.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

inline fun Context.toast(@StringRes res: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, res, duration).show()
