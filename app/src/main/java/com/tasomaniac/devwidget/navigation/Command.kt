package com.tasomaniac.devwidget.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.tasomaniac.devwidget.R
import com.tasomaniac.devwidget.extensions.toast

interface Command {
    fun action(activity: Activity)

    fun Intent.safeStart(activity: Activity) = activity.apply {
        try {
            startActivity(this@safeStart)
        } catch (e: ActivityNotFoundException) {
            toast(R.string.widget_error_activity_cannot_be_launched)
        } finally {
            finish()
        }
    }
}
