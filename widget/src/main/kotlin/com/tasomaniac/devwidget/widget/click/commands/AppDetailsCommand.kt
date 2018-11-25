package com.tasomaniac.devwidget.widget.click.commands

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.tasomaniac.devwidget.navigation.IntentCommand

internal data class AppDetailsCommand(private val packageName: String) : IntentCommand {
    override fun createIntent(context: Context): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
    }
}
