package com.tasomaniac.devdrawer.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun Intent.toPendingActivity(context: Context) =
    PendingIntent.getActivity(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)

fun Intent.toPendingBroadcast(context: Context) =
    PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
