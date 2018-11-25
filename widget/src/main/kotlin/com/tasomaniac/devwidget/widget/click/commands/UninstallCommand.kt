package com.tasomaniac.devwidget.widget.click.commands

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tasomaniac.devwidget.navigation.IntentCommand

internal data class UninstallCommand(private val packageName: String) : IntentCommand {

    override fun createIntent(context: Context) =
        Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
            data = Uri.parse("package:$packageName")
        }
}
