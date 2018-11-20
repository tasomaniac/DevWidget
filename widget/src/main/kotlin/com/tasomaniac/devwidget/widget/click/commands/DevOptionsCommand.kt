package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import com.tasomaniac.devwidget.navigation.Command

object DevOptionsCommand : Command {
    override fun action(activity: Activity) {
        Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS).safeStart(activity)
    }
}
