package com.tasomaniac.devwidget.navigation

import android.app.Activity
import javax.inject.Inject

class Navigator @Inject constructor(
    private val activity: Activity
) {

    fun navigate(command: Command) {
        command.action(activity)
    }
}
