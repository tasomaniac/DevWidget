package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import com.tasomaniac.devwidget.navigation.Command
import com.tasomaniac.devwidget.widget.click.WidgetRefreshActivity

data class WidgetRefreshCommand(private val appWidgetId: Int) : Command {

    override fun action(activity: Activity) {
        val intent = WidgetRefreshActivity.createIntent(activity, appWidgetId)
        activity.startActivity(intent)
    }
}
