package com.tasomaniac.devwidget.widget.click.commands

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import com.tasomaniac.devwidget.navigation.Command

data class ComponentCommand(private val component: ComponentName) : Command {

    override fun action(activity: Activity) {
        Intent().setComponent(component).start(activity)
    }
}
