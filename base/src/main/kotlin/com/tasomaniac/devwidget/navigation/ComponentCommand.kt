package com.tasomaniac.devwidget.navigation

import android.content.ComponentName
import android.content.Context
import android.content.Intent

data class ComponentCommand(private val component: ComponentName) : IntentCommand {

    override fun createIntent(context: Context) = Intent().setComponent(component)
}
