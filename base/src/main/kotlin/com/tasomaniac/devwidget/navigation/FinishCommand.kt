package com.tasomaniac.devwidget.navigation

import android.app.Activity

object FinishCommand : Command {
    override fun start(activity: Activity) {
        activity.finish()
    }
}
