package com.tasomaniac.devwidget.widget.click.commands

import android.content.Context
import android.os.UserHandle
import com.tasomaniac.devwidget.navigation.IntentCommand
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity

internal data class ActivityChooserCommand(
    private val packageName: String,
    private val user: UserHandle
) : IntentCommand {

    override fun createIntent(context: Context) =
        ActivityChooserActivity.createIntent(context, packageName, user)
}
