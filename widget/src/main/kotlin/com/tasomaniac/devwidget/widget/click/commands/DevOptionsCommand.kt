package com.tasomaniac.devwidget.widget.click.commands

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.tasomaniac.devwidget.navigation.IntentCommand

internal object DevOptionsCommand : IntentCommand {

    override fun createIntent(context: Context) = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
}
