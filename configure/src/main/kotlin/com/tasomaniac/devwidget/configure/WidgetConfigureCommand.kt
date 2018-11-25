package com.tasomaniac.devwidget.configure

import android.content.Context
import com.tasomaniac.devwidget.navigation.IntentCommand

data class WidgetConfigureCommand(private val appWidgetId: Int) : IntentCommand {

    override fun createIntent(context: Context) = ConfigureActivity.createIntent(context, appWidgetId)
}
