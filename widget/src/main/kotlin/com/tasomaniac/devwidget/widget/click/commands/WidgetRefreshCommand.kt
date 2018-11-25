package com.tasomaniac.devwidget.widget.click.commands

import android.content.Context
import com.tasomaniac.devwidget.navigation.IntentCommand
import com.tasomaniac.devwidget.widget.click.WidgetRefreshActivity

internal data class WidgetRefreshCommand(private val appWidgetId: Int) : IntentCommand {

    override fun createIntent(context: Context) = WidgetRefreshActivity.createIntent(context, appWidgetId)
}
