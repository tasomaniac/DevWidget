package com.tasomaniac.devwidget.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun Intent.toPendingActivity(context: Context, requestCode: Int = 0) =
    PendingIntent.getActivity(context, requestCode, this, 0)

fun Intent.toPendingBroadcast(context: Context) =
    PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
