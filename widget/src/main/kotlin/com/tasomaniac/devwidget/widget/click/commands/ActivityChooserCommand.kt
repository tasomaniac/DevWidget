package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.os.UserHandle
import com.tasomaniac.devwidget.navigation.Command
import com.tasomaniac.devwidget.widget.chooser.ActivityChooserActivity

data class ActivityChooserCommand(
    private val packageName: String,
    private val user: UserHandle
) : Command {

    override fun action(activity: Activity) {
        ActivityChooserActivity.createIntent(activity, packageName, user)
            .safeStart(activity)
    }
}
