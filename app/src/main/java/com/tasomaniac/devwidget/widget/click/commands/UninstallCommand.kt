package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.tasomaniac.devwidget.navigation.Command

data class UninstallCommand(private val packageName: String) : Command {
    override fun action(activity: Activity) {
        Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:$packageName")
        }.safeStart(activity)
    }
}
