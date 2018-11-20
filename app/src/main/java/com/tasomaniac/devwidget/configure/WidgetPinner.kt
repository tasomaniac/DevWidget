package com.tasomaniac.devwidget.configure

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import com.tasomaniac.devwidget.extensions.toPendingBroadcast
import com.tasomaniac.devwidget.widget.WidgetProvider
import javax.inject.Inject

class WidgetPinner @Inject constructor(
    private val app: Application,
    private val appWidgetManager: AppWidgetManager
) {

    @RequiresApi(O)
    fun requestPin() {
        val widgetProvider = ComponentName(app, WidgetProvider::class.java)
        val successCallback = Intent(app, WidgetPinnedReceiver::class.java)
            .toPendingBroadcast(app)

        appWidgetManager.requestPinAppWidget(widgetProvider, null, successCallback)
    }
}
