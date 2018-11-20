package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import com.tasomaniac.devwidget.navigation.Command

object FinishCommand : Command {
    override fun action(activity: Activity) {
        activity.finish()
    }
}
