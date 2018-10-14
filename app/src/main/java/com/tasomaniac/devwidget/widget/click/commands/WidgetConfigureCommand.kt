package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import com.tasomaniac.devwidget.configure.ConfigureActivity
import com.tasomaniac.devwidget.navigation.Command

data class WidgetConfigureCommand(private val appWidgetId: Int) : Command {

    override fun action(activity: Activity) {
        ConfigureActivity.createIntent(activity, appWidgetId).safeStart(activity)
    }
}
