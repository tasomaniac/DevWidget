package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.tasomaniac.devwidget.navigation.Command

data class AppDetailsCommand(private val packageName: String) : Command {
    override fun action(activity: Activity) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }.start(activity)
    }
}
