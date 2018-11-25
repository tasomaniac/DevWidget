package com.tasomaniac.devwidget.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent

interface IntentCommand : Command {

    fun createIntent(context: Context): Intent

    override fun start(activity: Activity) {
        createIntent(activity).safeStart(activity)
    }
}
