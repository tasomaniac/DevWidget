package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.tasomaniac.devwidget.navigation.Command

data class PlayStoreCommand(private val packageName: String) : Command {

    override fun action(activity: Activity) {
        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            .start(activity)
    }
}
